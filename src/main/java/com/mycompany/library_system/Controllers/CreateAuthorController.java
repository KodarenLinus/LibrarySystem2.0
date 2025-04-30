/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Logic.AddAuthor;
import com.mycompany.library_system.Models.Author;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 *
 * @author Linus
 */
public class CreateAuthorController {
    
    @FXML
    private TextField AuthorFirstname;

    @FXML
    private TextField AuthorLastname;

    @FXML
    void CreateAuthor(ActionEvent event) {
        String firstname = AuthorFirstname.getText();
        String lastname = AuthorLastname.getText();
        Author author = new Author(firstname, lastname);
        AddAuthor addAuthor = new AddAuthor();
        
        addAuthor.insertAuthor(author);
    }
    
}
