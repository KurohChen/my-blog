package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

// 给bean一个名字：alphaHibernate
@Repository("alphaHibernate")
public class AlphaDaoHibernateImpl implements AlphaDao{

    @Override
    public String select() {
        return "Hibernate";
    }
}
