package com.cw.outsourcing.outsourcingbackend.pojo.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickName;

    private String userName;

    private String password;

    private String email;

    @Column(name = "is_enabled")
    private Integer enabled;

    private Date createTime;

    private Date updateTime;

}
