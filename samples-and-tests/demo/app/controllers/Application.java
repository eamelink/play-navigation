package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import navigation.MenuItem;
import navigation.Navigation;

import models.*;

@With(MenuInjector.class)
public class Application extends Controller {
    public static void index() {
        render();
    }
}