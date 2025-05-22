package com.example;

import com.example.util.HibernateUtil;
import org.hibernate.SessionFactory;

public class TestHibernate {
    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        System.out.println("SessionFactory created successfully: " + (sessionFactory != null));
        HibernateUtil.shutdown();
    }
}