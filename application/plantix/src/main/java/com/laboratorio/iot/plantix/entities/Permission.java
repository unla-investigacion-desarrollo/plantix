package com.laboratorio.iot.plantix.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Permission {
    private Long id;
    private String permissionName;
    private List<Role> roles;
}
