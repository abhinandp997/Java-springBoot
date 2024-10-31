package com.crudtask.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.crudtask.demo.dao.RoleDao;
import com.crudtask.demo.dao.StatusDao;
import com.crudtask.demo.dao.UserDao;
import com.crudtask.demo.entity.Role;
import com.crudtask.demo.entity.Status;
import com.crudtask.demo.entity.User;
import com.crudtask.demo.user.WebUser;

@Service
public class UserServiceImpl implements UserService {

    private UserDao userDao;
    private RoleDao roleDao;
    private StatusDao statusDao;
    private BCryptPasswordEncoder passwordEncoder;
	private Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleDao roleDao, BCryptPasswordEncoder passwordEncoder, StatusDao statusDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
        this.statusDao = statusDao;
    }

    @Override
    public User findByUserName(String userName) {
        return userDao.findByUserName(userName);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Attempting to load "+username);
        User user = userDao.findByUserName(username);
        if (user == null) {
			logger.info("User not found ");
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.addAll(mapRolesToAuthorities(user.getRoles()));
        // authorities.addAll(mapStatusToAuthorities(user.getStatus()));

		logger.info("User  found ");

        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), authorities);
    }

    private Collection<SimpleGrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role tempRole : roles) {
            SimpleGrantedAuthority tempAuthority = new SimpleGrantedAuthority(tempRole.getName());
            authorities.add(tempAuthority);
        }
        return authorities;
    }

    private Collection<SimpleGrantedAuthority> mapStatusToAuthorities(Collection<Status> statuses) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Status tempStatus : statuses) {
            SimpleGrantedAuthority tempAuthority = new SimpleGrantedAuthority("STATUS_" + tempStatus.getStatusName());
            authorities.add(tempAuthority);
        }
        return authorities;
    }

    @Override
    public User findByUserId(int id) {
        return userDao.findByUserId(id);
    }

    @Override
    public List<User> findAllUser() {
        return userDao.findAllUser();
    }

    @Override
    public Status findStatusByName(String statusName) {
        return statusDao.findStatusByName(statusName);
    }

	@Override
	public void save(WebUser webUser){
		User user = new User();

        user.setUserName(webUser.getUserName());
        user.setPassword(passwordEncoder.encode(webUser.getPassword()));
        user.setFirstName(webUser.getFirstName());
        user.setLastName(webUser.getLastName());
        user.setEmail(webUser.getEmail());
        user.setRoles(Arrays.asList(roleDao.findRoleByName("ROLE_EMPLOYEE")));
        user.setStatus(Arrays.asList(statusDao.findStatusByName("PENDING")));
        user.setEnabled(true);

        userDao.save(user);

		
	}

    // @Override
    // public void updateUser(User user) {
        
    //     User existingUser  = userDao.findByUserId(user.getId());
    //     logger.info("In UpdateUser");
    //     if(existingUser != null){
    //         logger.info("In UpdateUser existing user found "+existingUser.getUserName());
    //         existingUser.setUserName(user.getUserName());
    //         existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
    //         existingUser.setFirstName(user.getFirstName());
    //         existingUser.setLastName(user.getLastName());
    //         existingUser.setStatus(Arrays.asList(statusDao.findStatusByName("SUBMITTED")));
    //         existingUser.setEmail(user.getEmail());

    //         userDao.update(existingUser);
    //     }
    // }
    @Override
    public void updateUser(User user) {
        User existingUser = userDao.findByUserId(user.getId());
        if (existingUser != null) {
            existingUser.setUserName(user.getUserName());
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setStatus(new ArrayList<>(Arrays.asList(statusDao.findStatusByName("SUBMITTED"))));
            existingUser.setEmail(user.getEmail());
            userDao.update(existingUser);
        }
    }

    @Override
    public void approveUser(int id) {
       User existingUser = userDao.findByUserId(id);
       if(existingUser != null){
        existingUser.setStatus(new ArrayList<>(Arrays.asList(statusDao.findStatusByName("APPROVED"))));
        userDao.update(existingUser);
       }
    }

    @Override
    public void rejectUser(int id) {
       User existingUser = userDao.findByUserId(id);
       if(existingUser != null){
        existingUser.setStatus(new ArrayList<>(Arrays.asList(statusDao.findStatusByName("REJECTED"))));
        userDao.update(existingUser);
       }
    }

    @Override
    public void deleteUser(int id) {
        logger.info("Inside deleteUser........");
        userDao.delete(id);
    }

}
