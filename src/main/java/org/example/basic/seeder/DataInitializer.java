package org.example.basic.seeder;

import lombok.RequiredArgsConstructor;
import org.example.basic.entities.Permission;
import org.example.basic.entities.Role;
import org.example.basic.entities.RoleName;
import org.example.basic.repositories.PermissionRepository;
import org.example.basic.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (roleRepository.count() > 0) return;   // đã seed rồi thì bỏ qua

        Permission userRead = perm("user:read", "Xem thông tin người dùng");
        Permission userWrite = perm("user:write", "Tạo / cập nhật người dùng");
        Permission userDelete = perm("user:delete", "Xóa người dùng");
        Permission reportRead = perm("report:read", "Xem báo cáo");
        Permission reportWrite = perm("report:write", "Tạo / cập nhật báo cáo");
        Permission adminAll = perm("admin:all", "Toàn quyền quản trị");

        permissionRepository.saveAll(List.of(
                userRead, userWrite, userDelete,
                reportRead, reportWrite, adminAll
        ));

        Role roleUser = Role.builder()
                .name(RoleName.ROLE_USER)
                .description("Người dùng thông thường")
                .permissions(Set.of(userRead, reportRead))
                .build();

        Role roleManager = Role.builder()
                .name(RoleName.ROLE_MANAGER)
                .description("Quản lý")
                .permissions(Set.of(userRead, userWrite, reportRead, reportWrite))
                .build();

        Role roleAdmin = Role.builder()
                .name(RoleName.ROLE_ADMIN)
                .description("Quản trị viên")
                .permissions(Set.of(
                        userRead, userWrite, userDelete,
                        reportRead, reportWrite, adminAll
                ))
                .build();

        roleRepository.saveAll(List.of(roleUser, roleManager, roleAdmin));
    }

    private Permission perm(String name, String desc) {
        return Permission.builder().name(name).description(desc).build();
    }
}
