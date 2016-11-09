/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.05
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.model;

import java.io.Serializable;

public class Game implements Serializable
{
    // fields

    private String id, name, status;

    // constructors

    public Game()
    {
    }

    // methods

    public String getID()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public String toString()
    {
        return name + " - " + status;
    }

    public void setID(String id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setStatus(String status)
    {
        if (status.equalsIgnoreCase("waiting"))
        {
            this.status = "Waiting";
        }
        else if (status.equalsIgnoreCase("playing"))
        {
            this.status = "In Progress";
        }
        else
        {
            this.status = "Game Over";
        }
    }
}