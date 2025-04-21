package org.mkab.EventManagement.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "jamat")
public class Jamat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "next_val", nullable = false)
    private Integer nextVal = 1000; // Default starting value

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setName(String name2) {
		// TODO Auto-generated method stub
		
	}

	public String getNextVal() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setNextVal(String nextVal2) {
		// TODO Auto-generated method stub
		
	}

    // Constructors, getters, and setters are handled by Lombok @Data
}