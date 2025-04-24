package com.eli.ads.user.role;

import com.eli.ads.user.permission.PermissionEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum RoleEnum {

    OFFICE_MANAGER(
            Set.of(
                    PermissionEnum.VIEW_APPOINTMENT,
                    PermissionEnum.BOOK_APPOINTMENT,
                    PermissionEnum.REGISTER_PATIENT,
                    PermissionEnum.REGISTER_DENTIST,
                    PermissionEnum.LIST_PATIENTS,
                    PermissionEnum.VIEW_PATIENT,
                    PermissionEnum.UPDATE_PATIENT,
                    PermissionEnum.DELETE_PATIENT,
                    PermissionEnum.LIST_ADDRESSES,
                    PermissionEnum.LIST_DENTISTS
            )
    ),

    DENTIST(
            Set.of(
                    PermissionEnum.VIEW_APPOINTMENT
            )
    ),

    PATIENT(
            Set.of(
                    PermissionEnum.BOOK_APPOINTMENT,
                    PermissionEnum.VIEW_APPOINTMENT,
                    PermissionEnum.CANCEL_APPOINTMENT,
                    PermissionEnum.RESCHEDULE_APPOINTMENT
            )
    ),

    ADMIN(
            Set.of(PermissionEnum.values()) // All permissions
    );

    private final Set<PermissionEnum> permissions;

}
