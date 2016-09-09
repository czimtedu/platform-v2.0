/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.framework.security;

import com.platform.modules.sys.bean.SysPermission;
import com.platform.modules.sys.bean.SysRole;
import com.platform.modules.sys.bean.SysUser;
import com.platform.modules.sys.action.LoginAction;
import com.platform.modules.sys.service.PermissionService;
import com.platform.modules.sys.service.RoleService;
import com.platform.modules.sys.service.UserService;
import com.platform.framework.security.shiro.session.SessionDAO;
import com.platform.framework.servlet.ValidateCodeServlet;
import com.platform.framework.util.Encodes;
import com.platform.framework.util.StringUtils;
import com.platform.modules.sys.utils.UserUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 用户身份验证,授权 Realm 组件
 *
 * @author lufengc
 * @date 2015-6-16
 **/
@Service
public class SecurityRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private SessionDAO sessionDao;

    /**
     * 登录验证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;

        int activeSessionSize = sessionDao.getActiveSessions(false).size();
        if (logger.isDebugEnabled()) {
            logger.debug("login submit, active session size: {}, username: {}", activeSessionSize, token.getUsername());
        }

        // 校验登录验证码
        if (LoginAction.isValidateCodeLogin(token.getUsername(), false, false)) {
            Session session = UserUtils.getSession();
            String code = null;
            if (session != null) {
                code = (String) session.getAttribute(ValidateCodeServlet.VALIDATE_CODE);
            }
            if (token.getCaptcha() == null || !token.getCaptcha().toUpperCase().equals(code)) {
                throw new AuthenticationException("msg:验证码错误, 请重试.");
            }
        }
        // 校验用户名密码
        SysUser user = userService.getByUsername(token.getUsername());
        if (user != null) {
            if (user.getStatus() == 0) {
                throw new AuthenticationException("msg:该已帐号禁止登录.");
            }
            byte[] salt = Encodes.decodeHex(user.getPassword().substring(0, 16));
            return new SimpleAuthenticationInfo(new Principal(user, token.isMobileLogin()),
                    user.getPassword().substring(16), ByteSource.Util.bytes(salt), getName());
        } else {
            return null;
        }
    }

    /**
     * 权限检查
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Principal principal = (Principal) getAvailablePrincipal(principals);

    	/*if (!Global.TRUE.equals(SysConfigManager.getInstance().getConfig("user.multiAccountLogin"))){
			Collection<Session> sessions = sessionDao.getActiveSessions(true, principal, UserUtils.getSession());
			if (sessions.size() > 0){
				// 如果是登录进来的，则踢出已在线用户
				if (UserUtils.getSubject().isAuthenticated()){
					for (Session session : sessions){
						sessionDao.delete(session);
					}
				}else{// 记住我进来的，并且当前用户已登录，则退出当前用户提示信息。
					UserUtils.getSubject().logout();
					throw new AuthenticationException("msg:账号已在其它地方登录，请重新登录。");
				}
			}
		}*/
        final SysUser user = userService.getByUsername(principal.getLoginName());
        if (user != null) {
            SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
            // 添加基于Permission的权限信息
            List<SysPermission> list = UserUtils.getMenuList();
            for (SysPermission permission : list){
                if (StringUtils.isNotBlank(permission.getPermissionSign())){
                    for (String sign : StringUtils.split(permission.getPermissionSign(), ",")){
                        authorizationInfo.addStringPermission(sign);
                    }
                }
            }
            // 添加用户权限
            authorizationInfo.addStringPermission("user");
            // 添加用户角色信息
            for (SysRole role : user.getRoleList()){
                authorizationInfo.addRole(role.getRoleSign());
            }
            return authorizationInfo;
        } else {
            return null;
        }
    }

    @Override
    protected void checkPermission(Permission permission, AuthorizationInfo info) {
        authorizationValidate(permission);
        super.checkPermission(permission, info);
    }

    @Override
    protected boolean[] isPermitted(List<Permission> permissions, AuthorizationInfo info) {
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
                authorizationValidate(permission);
            }
        }
        return super.isPermitted(permissions, info);
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, Permission permission) {
        authorizationValidate(permission);
        return super.isPermitted(principals, permission);
    }

    @Override
    protected boolean isPermittedAll(Collection<Permission> permissions, AuthorizationInfo info) {
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
                authorizationValidate(permission);
            }
        }
        return super.isPermittedAll(permissions, info);
    }

    /**
     * 授权验证方法
     *
     * @param permission
     */
    private void authorizationValidate(Permission permission) {
        // 模块授权预留接口
    }

    /**
     * 设定密码校验的Hash算法与迭代次数
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(Encodes.HASH_ALGORITHM);
        matcher.setHashIterations(Encodes.HASH_INTERATIONS);
        setCredentialsMatcher(matcher);
    }

    /**
     * 清空用户关联权限认证，待下次使用时重新加载
     */
	/*public void clearCachedAuthorizationInfo(Principal principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}*/

    /**
     * 清空所有关联认证
     * @Deprecated 不需要清空，授权缓存保存到session中
     */
	/*@Deprecated
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}*/

    /**
     * 授权用户信息
     */
    public static class Principal implements Serializable {

        private static final long serialVersionUID = 1L;

        private Integer id; // 编号
        private String loginName; // 登录名
        private String name; // 姓名
        private boolean mobileLogin; // 是否手机登录

        public Principal(SysUser user, boolean mobileLogin) {
            this.id = user.getId();
            this.loginName = user.getUsername();
            this.name = user.getUsername();
            this.mobileLogin = mobileLogin;
        }

        public Integer getId() {
            return id;
        }

        public String getLoginName() {
            return loginName;
        }

        public String getName() {
            return name;
        }

        public boolean isMobileLogin() {
            return mobileLogin;
        }

        /**
         * 获取SESSIONID
         */
        public String getSessionid() {
            try {
                return (String) UserUtils.getSession().getId();
            } catch (Exception e) {
                return "";
            }
        }

        @Override
        public String toString() {
            return id.toString();
        }

    }

}
