package com.alo.savemytrip.repositories;

import android.arch.lifecycle.LiveData;

import com.alo.savemytrip.database.dao.UserDao;
import com.alo.savemytrip.models.User;

public class UserDataRepository {

    private final UserDao userDao;

    public UserDataRepository(UserDao userDao) { this.userDao = userDao; }

    // --- GET USER ---
    public LiveData<User> getUser(long userId) { return this.userDao.getUser(userId); }
}