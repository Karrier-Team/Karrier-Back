package com.karrier.mentoring.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Fallow")
@Getter
@Setter
public class Fallow {

    private String fallowEmail;
}
