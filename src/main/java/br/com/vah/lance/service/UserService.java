package br.com.vah.lance.service;

import javax.ejb.Stateless;

import br.com.vah.lance.entity.User;

/**
 *
 * @author Emre Simtay <emre@simtay.com>
 */

@Stateless
public class UserService extends DataAccessService<User>{
    
    public UserService(){
        super(User.class);
    }   
}
