package returnobjects;

import javax.validation.constraints.Size;

public class Account {
        
    @Size(max=50)
    private String username, password;

    private boolean enabled;
    
    private long entity;
    
    public Account() {}
        
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public boolean getEnabled() {
        return this.enabled;
    }
    
    public void setEntity(long entity) {
        this.entity = entity;
    }
    
    public long getEntity() {
        return this.entity;
    }
    
}
