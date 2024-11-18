package role;

public class exes {
    public static Boolean allex(User user) {
        if(user==null){
            return false;
        }
        return  user.getRole() == Role.ADMIN || user.getRole() == Role.OWNER;
    }

    public static Boolean ratar_menuex(User user) {
        if(user==null){
            return false;
        }
        return user.getRole() == Role.BUSINESS_MANAGER || allex(user);
    }

    public static Boolean asztalex(User user) {
        if(user==null){
            return false;
        }
        return user.getRole() == Role.WAITER || ratar_menuex(user);
    }

    public static Boolean menuex(User user) {
        if(user==null){
            return false;
        }
        return user.getRole() == Role.CHEF || ratar_menuex(user);
    }


}
