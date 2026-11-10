package com.lindakey.applications;

import com.lindakey.users.User;
import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 50)
    private String environment = "Development";

    @Column(name = "upstream_target_url", nullable = false, length = 500)
    private String upstreamTargetUrl;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;

    public Application() {}

    public Application(User user, String name, String environment, String upstreamTargetUrl) {
        this.user = user;
        this.name = name;
        this.environment = environment;
        this.upstreamTargetUrl = upstreamTargetUrl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }

    public String getUpstreamTargetUrl() { return upstreamTargetUrl; }
    public void setUpstreamTargetUrl(String upstreamTargetUrl) { this.upstreamTargetUrl = upstreamTargetUrl; }

    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }
}