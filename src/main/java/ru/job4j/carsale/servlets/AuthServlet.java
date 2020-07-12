package ru.job4j.carsale.servlets;

import ru.job4j.carsale.models.User;
import ru.job4j.carsale.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class AuthServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        List<User> userList = PsqlStore.instOf().findUserByLogin(login);
        if (!userList.isEmpty()) {
            HttpSession sc = req.getSession();
            sc.setAttribute("user", login);
            sc.setAttribute("userUUID", userList.get(0).getId());
            resp.setHeader("userUUID", String.valueOf(userList.get(0).getId()));
        } else {
            resp.setStatus(401);
            req.setAttribute("error", "Не верный email или пароль");
        }
    }
}
