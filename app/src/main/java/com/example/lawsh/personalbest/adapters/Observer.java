package com.example.lawsh.personalbest.adapters;

import java.util.Set;

public interface Observer {
    public void update(Set<String> friends, Set<String> pendingFriends);
}
