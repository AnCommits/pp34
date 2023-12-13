package ru.an.pp33.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import ru.an.pp33.constants.RolesType;

import javax.persistence.*;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonBackReference
    private Set<User> users;

    public Role(String name) {
        setName(name);
    }

    public void setName(String name) {
        String nameInUpperCase = name.toUpperCase();
        this.name = RolesType.allRolesNames.contains(nameInUpperCase)
                ? nameInUpperCase
                : RolesType.allRolesNames.get(RolesType.allRolesNames.size() - 1);
    }

    @Override
    public String getAuthority() {
        return name;
    }

    public static Comparator<Role> roleComparator = new Comparator<Role>() {
        @Override
        public int compare(Role o1, Role o2) {
            return RolesType.allRolesNames.lastIndexOf(o1.getName()) -
                    RolesType.allRolesNames.lastIndexOf(o2.getName());
        }
    };

    @Override
    public String toString() {
        return "Role{" + "id=" + id + ", name='" + name + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return name.equals(role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
