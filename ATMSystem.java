package com.itheima;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ATMSystem {
    public static void main(String[] args) {
        ArrayList<Account> accounts = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("=================黑马ATM系统===================");
            System.out.println("1、账户登录");
            System.out.println("2、账户开户");
            System.out.println("请您选择操作：");
            int command = sc.nextInt();
            switch (command) {
                case 1:
                    // 用户登录
                    login(accounts, sc);
                    break;
                case 2:
                    // 用户注册
                    register(accounts, sc);
                    break;
                default:
                    System.out.println("您输入的操作命令不存在！！！");
            }
        }

    }

    /**
     * 用户登录功能
     * @param accounts 全部账户对象
     * @param sc 扫描器
     */

    private static void login(ArrayList<Account> accounts, Scanner sc) {
        System.out.println("==================欢迎进入ATM银行登录页面===================");
        // 首先有没有账户
        if (accounts.isEmpty()) {
            System.out.println("对不起！当前系统重没有任何账户，请先开户！");
            return;
        }

        while (true) {
            System.out.println("请您输入登录卡号：");
            String cardId = sc.next();
            Account acc = getAccountByCardId(cardId, accounts);
            if (acc != null) {
                // 卡号存在
                while (true) {
                    System.out.println("请您输入密码：");
                    String password = sc.next();
                    if (password.equals(acc.getPassword())) {
                        // 登录成功
                        System.out.println("恭喜您，" + acc.getUserName() + "先生/女士进入系统，您的卡号是：" + acc.getCardId());
                        // ...查询、转账、取款
                        // 展示登录后的操作页
                        showUserCommand(sc, acc, accounts);
                        return;
                    }else {
                        System.out.println("您输入的密码有误！！！");
                    }
                }


            }else {
                System.out.println("对不起！系统中不存在该卡号~~~");
            }
        }


    }

    /**
     * 登录后的操作页
     */
    private static void showUserCommand(Scanner sc, Account acc, ArrayList<Account> accounts) {
        while (true) {
            System.out.println("==================用户操作页面===================");
            System.out.println("1、查询");
            System.out.println("2、存款");
            System.out.println("3、取款");
            System.out.println("4、转账");
            System.out.println("5、修改密码");
            System.out.println("6、退出");
            System.out.println("7、注销当前账户");
            System.out.println("请您选择操作功能：");
            int command = sc.nextInt();
            switch (command) {
                case 1:
                    // 查询（展示当前登录的账户）
                    showAccount(acc);
                    break;
                case 2:
                    // 存款
                    depositMoney(acc, sc);
                    break;
                case 3:
                    // 取款
                    drawMoney(acc, sc);
                    break;
                case 4:
                    // 转账
                    transferMoney(sc, acc, accounts);
                    break;
                case 5:
                    // 修改密码
                    updatePassword(sc, acc);
                    return;
                case 6:
                    // 退出
                    System.out.println("退出成功，欢迎下次光临~~~");
                    return;
                case 7:
                    if (deleteAccount(acc, accounts, sc)) {
                        return;
                    }else {
                        break;
                    }
                default:
                        System.out.println("您输入的操作命令不正确~~~");
            }
        }

    }

    /**
     * 销户功能
     * @param acc 当前账户
     * @param accounts 全部账户对象集合
     * @param sc 扫描器
     */
    private static boolean deleteAccount(Account acc, ArrayList<Account> accounts, Scanner sc) {
        System.out.println("==================欢迎进入用户销户页面===================");
        // 注销当前账户
        System.out.println("您确定要销户吗？(y/n)");
        String rs = sc.next();
        switch (rs) {
            case "y":
                // 销户
                if (acc.getMoney() > 0) {
                    System.out.println("当前账户余额大于0，不能进行销户~~~");
                }else {
                    accounts.remove(acc);
                    System.out.println("您的账户销户成功~~~");
                    return true;
                }
                break;
            default:
                System.out.println("好的，当前账户继续保留~~~");
        }
        return false;
    }

    /**
     * 修改密码功能
     * @param sc 扫描器
     * @param acc 当前登录的账户
     */

    private static void updatePassword(Scanner sc, Account acc) {
        System.out.println("==================欢迎进入用户密码修改页面===================");
        while (true) {
            System.out.println("请您输入当前密码：");
            String password = sc.next();
            if (acc.getPassword().equals(password)) {
                while (true) {
                    System.out.println("请您输入新密码：");
                    String newPassword = sc.next();
                    System.out.println("请你确认新密码：");
                    String okPassword = sc.next();
                    if (okPassword.equals(newPassword)) {
                        // 2次密码输入一致
                        acc.setPassword(newPassword);
                        System.out.println("密码修改成功！！！");
                        return;
                    }else {
                        System.out.println("您2次输入的密码不一致~~~");
                    }
                }

            }else {
                System.out.println("您输入的密码不正确~~~");
            }
        }

    }

    /**
     * 转账功能
     * @param sc 扫描器
     * @param acc 当前登录账户
     * @param accounts 全部账户
     */
    private static void transferMoney(Scanner sc, Account acc, ArrayList<Account> accounts) {
        System.out.println("==================欢迎进入用户转账页面===================");
        // 1、首先判断当前系统中是否有至少2个账户
        if (accounts.size() < 2) {
            System.out.println("当前系统中不足2个账户，赶紧取开户去吧！！！");
            return;
        }
        // 2、开始转账
        if (acc.getMoney() < 0) {
            System.out.println("对不起，当前账户余额为0，不能转账~~~");
            return;
        }
        while (true) {
            // 3、真正开始转账
            System.out.println("请您输入对方账户的卡号：");
            String cardId = sc.next();
            if (cardId.equals(acc.getCardId())) {
                System.out.println("对不起，您不能给自己账号转账~~~");
                continue;
            }
            Account account = getAccountByCardId(cardId, accounts);
            if (account == null) {
                System.out.println("对不起，您输入的账号不存在~~~");
            }else {
                String userName = account.getUserName();
                String tip = "*" + userName.substring(1);
                System.out.println("请您输入[" + tip + "]的姓氏！！！");
                String preName = sc.next();
                if (userName.startsWith(preName)) {
                    System.out.println("请您输入转账金额：");
                    double money = sc.nextDouble();
                    if (money > acc.getMoney()) {
                        System.out.println("对不起，您当前账户余额不足，您最多可转" + acc.getMoney() + "元。");
                    }else {
                        acc.setMoney(acc.getMoney() - money);
                        account.setMoney(account.getMoney() + money);
                        System.out.println("转账成功！您的账户还剩" + acc.getMoney() + "元！");
                        return;
                    }


                }else {
                    System.out.println("对不起，您输入的信息有误~~~");
                }


            }
        }

    }

    /**
     * 取款功能
     * @param acc 当前账户对象
     * @param sc 扫描器
     */
    private static void drawMoney(Account acc, Scanner sc) {
        System.out.println("==================欢迎进入用户取款页面===================");
        if (acc.getMoney() < 100) {
            System.out.println("对不起，当前余额不足100元，不能取钱！！！");
            return;
        }
        while (true) {
            System.out.println("请输入取款金额：");
            double money = sc.nextDouble();
            if (money > acc.getQuotaMoney()) {
                System.out.println("对不起，您当前取款金额超出每次限额，每次最多可取：" + acc.getQuotaMoney());
            }else {
                System.out.println("恭喜您取" + money + "成功！！！");
                acc.setMoney(acc.getMoney() - money);
                showAccount(acc);
                return;
            }
        }

    }

    /**
     * 存款功能
     * @param acc 当前登录的账户
     * @param sc 扫描器
     */
    private static void depositMoney(Account acc, Scanner sc) {
        System.out.println("==================欢迎进入用户存款页面===================");
        System.out.println("请输入存款金额：");
        double money = sc.nextDouble();
        acc.setMoney(acc.getMoney() + money);
        System.out.println("恭喜您存款成功！");
        showAccount(acc);
    }

    /**
     * 展示账户信息
     * @param acc 当前登录账户
     */
    private static void showAccount(Account acc) {
        System.out.println("==================当前账户信息如下===================");
        System.out.println("卡号：" + acc.getCardId());
        System.out.println("户主：" + acc.getUserName());
        System.out.println("余额：" + acc.getMoney());
        System.out.println("限额：" + acc.getQuotaMoney());
    }

    /**
     账户开户功能
     * @param accounts 全部账户对象
     */
    private static void register(ArrayList<Account> accounts, Scanner sc) {
        System.out.println("==================系统开户操作页面===================");
        Account account = new Account();

        System.out.println("请您输入账户用户名：");
        String userName = sc.next();
        account.setUserName(userName);

        while (true) {
            System.out.println("请您输入账户密码：");
            String password = sc.next();
            System.out.println("请您确认账户密码：");
            String okPassword = sc.next();
            if (okPassword.equals(password)) {
                account.setPassword(okPassword);
                break;
            }else {
                System.out.println("对不起！您2次输入的密码不一致，请重新输入");
            }
        }

        System.out.println("请您输入账户当次限额：");
        double quotaMoney = sc.nextDouble();
        account.setQuotaMoney(quotaMoney);

        // 为账户随机生成一个8位且与其他账户不一致的卡号
        String cardId = getRandemCardId(accounts);
        account.setCardId(cardId);

        accounts.add(account);
        System.out.println("恭喜您，" + userName + "先生/女士开户成功，您的卡号是：" + cardId + "，请您妥善保管！！！");
    }

    /**
     * 随机生成一个卡号且与其他账户卡号不一致。
     * @return
     */
    private static String getRandemCardId(ArrayList<Account> accounts) {
        Random r = new Random();
        while (true) {

            String cardId = "";
            for (int i = 0; i < 8; i++) {
                int id = r.nextInt(10);
                cardId += id;
            }

            Account acc = getAccountByCardId(cardId, accounts);
            if (acc == null) {
                return cardId;
            }
        }
    }

    /**
     * 根据卡号查询一个账户对象出来
     * @param cardId 卡号
     * @param accounts 全部账号集合
     * @return 账号对象 | null
     */
    public static Account getAccountByCardId (String cardId, ArrayList<Account> accounts) {
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            if (acc.getCardId().equals(cardId)) {
                return acc;
            }
        }
        return null;
    }
}
