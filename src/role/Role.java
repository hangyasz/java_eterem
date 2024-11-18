package role;

public enum Role {
    WAITER,       // Can add new orders to tables
    ADMIN,        // Full system access, can change menu item states
    OWNER,        // Highest level of access, can manage everything
    BUSINESS_MANAGER, // Similar to owner, can manage most system functions
    CHEF          // Can add new menu items, cannot change their states
}
