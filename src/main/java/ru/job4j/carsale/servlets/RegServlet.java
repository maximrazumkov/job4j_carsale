package ru.job4j.carsale.servlets;

import ru.job4j.carsale.models.User;
import ru.job4j.carsale.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        User user = new User(login);
        PsqlStore.instOf().createUser(new User(login));
        HttpSession sc = req.getSession();
        sc.setAttribute("user", login);
        sc.setAttribute("userUUID", user.getId());
        req.getRequestDispatcher("index.html").forward(req, resp);
    }
}
