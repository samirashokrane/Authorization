package com.vispax.authorization.application.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(UserRoleId.class)
public class UserRole  implements Serializable  {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "user_id")
	private Long userId;
	@Id
	@Column(name = "role_id")
	private Integer roleId;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "user_id", referencedColumnName = "id" , insertable=false, updatable=false )
	@JsonIgnore
	User user;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "role_id", referencedColumnName = "id" , insertable=false, updatable=false )
	@JsonIgnore
	Role role;
	
	
}