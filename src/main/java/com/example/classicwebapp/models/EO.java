package com.example.classicwebapp.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EO {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long eoId;
  private String eoName;
  private String latinName;
  private String description;
  private int scentStrength;

  @ManyToOne
  private User user;

}
