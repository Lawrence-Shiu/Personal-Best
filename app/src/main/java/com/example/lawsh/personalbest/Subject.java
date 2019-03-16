package com.example.lawsh.personalbest;

import com.example.lawsh.personalbest.adapters.Observer;

public interface Subject {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObserver();
}
