package br.com.vah.lance.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import br.com.vah.lance.entity.usrdbvah.User;

/**
 * 
 * @author jairoportela
 *
 */
@Stateless
public class UserService extends DataAccessService<User> {
	
	public UserService(){
		super(User.class);
	}
	
	public User findByLogin(String login){
		Map<String, Object> params = new HashMap<>();
		params.put("login", login);
		List<User> resultList = findWithNamedQuery(User.FIND_BY_LOGIN, params);
		return resultList.isEmpty() ? null : resultList.get(0);
	}

}
