package org.mkab.EventManagement.Initialization;


import jakarta.annotation.PostConstruct;
import org.mkab.EventManagement.entity.Role;
import org.mkab.EventManagement.model.enums.RoleType;
import org.mkab.EventManagement.repository.RoleRepository;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DataInitializer {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void seedRoles() {
        Map<RoleType, String> roleDetails = Map.ofEntries(
            Map.entry(RoleType.SUPER_ADMIN, "Full system control"),
            Map.entry(RoleType.ADMIN, "Basic admin access"),
            Map.entry(RoleType.DATA_ENTRY, "Can enter data"),
            Map.entry(RoleType.DATA_PRINT, "Can print data and reports"),
            Map.entry(RoleType.CREATE_MEMBER, "Create new members"),
            Map.entry(RoleType.READ_MEMBER, "View member details"),
            Map.entry(RoleType.UPDATE_MEMBER, "Edit member info"),
            Map.entry(RoleType.DELETE_MEMBER, "Delete members"),
            Map.entry(RoleType.APPROVE_MEMBER, "Approve member registration"),
            Map.entry(RoleType.SEARCH_MEMBER, "Search members"),
            Map.entry(RoleType.CREATE_EVENT, "Create new events"),
            Map.entry(RoleType.VIEW_EVENT, "View event details"),
            Map.entry(RoleType.UPDATE_EVENT, "Edit event information"),
            Map.entry(RoleType.DELETE_EVENT, "Delete events"),
            Map.entry(RoleType.REGISTER_MEMBER, "Register members for events"),
            Map.entry(RoleType.PRINT_ID_CARD, "Print ID cards"),
            Map.entry(RoleType.PRINT_REPORT, "Print Reports"),
            Map.entry(RoleType.VIEW_REPORT, "View Reports")
        );

        roleDetails.forEach((type, detail) -> {
            roleRepository.findByType(type).orElseGet(() -> {
                Role role = new Role(type, detail);
                return roleRepository.save(role);
            });
        });

        System.out.println("✅ Role seed data initialized.");
    }
}
