package com.budget.tool;

import com.budget.tool.Controller.Client.BudgetController;
import com.budget.tool.Controller.Client.PayBillsController;

public class ControllerManager {
    private static BudgetController budgetControllerInstance;
    private static PayBillsController payBillsControllerInstance;

    public static void setBudgetController(BudgetController controller) {
        budgetControllerInstance = controller;
    }

    public static BudgetController getBudgetControllerInstance() {
        return budgetControllerInstance;
    }

    public static void setPayBillsController(PayBillsController controller) {
        payBillsControllerInstance = controller;
    }

    public static PayBillsController getPayBillsControllerInstance() {
        return payBillsControllerInstance;
    }
}
