package com.maksudrustamov.springboot.appwithjwt.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;



// через такой код, можно дать доступ только к блоку кода
@Service
public class AdminService {
    // к этому методу имеет доступ только админ
    // он всегда будет проверять,что у человека была ли эта роль
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void doAdminStuff(){
        System.out.println("Only admin here");
    }
}
