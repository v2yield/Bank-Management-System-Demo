import java.util.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.lang.String;
import java.io.*;

class Account {
    private String uid;//userID
    private String name;//name
    private String pwd;//password
    private String idid;//identityID
    private String num;//phone number
    private char sex;//sex
    private Date bd;//birthday
    private double blc;//balance
    public Account(String _uid, String _name, String _pwd, String _idid, String _num, char _sex, Date _bd) {
        uid=_uid;
        name=_name;
        pwd=_pwd;
        idid=_idid;
        num=_num;
        sex=_sex;
        bd=_bd;
        blc=2000.00;
    }
    public String getUID() {return uid;}
    public String getName() {return name;}
    public String getIDID() {return idid;}
    public String getPhoneNumber() {return num;}
    public char getSex() {return sex;}
    public String getBirthday() {return bd.toString();}
    public double getBalance() {return blc;}
    public void setName(String _name) {name=_name;}
    public void setPassword(String _pwd) {pwd=_pwd;}
    public void setPhoneNumber(String _num) {num=_num;}
    public void setBalance(double _blc) {blc=_blc;}
    public boolean matchPassword(String _pwd) {
        if (pwd.indexOf(_pwd)!=-1 && pwd.length()==_pwd.length()) {
            return true;
        }
        return false;
    }
}

class Bank {
    private ArrayList<Account> arraylist=new ArrayList<Account>();

    public Bank() {arraylist.ensureCapacity(10);}
    public int listSize() {return arraylist.size();}//to get the size of the account list
    public Account get(int index) {return arraylist.get(index);}//to get the account of a certain index
    public void add(Account account) {arraylist.add(account);}//to add a new account to the list
    public void delete(int index) {arraylist.remove(index);}//to delete a certain account
    public double balanceQuery(int index) {return get(index).getBalance();}//to check the balance of a certain account
    public void nameEdit(int index, String name_new) {get(index).setName(name_new);}//to edit the name
    public void passwordEdit(int index, String pwd_new) {get(index).setPassword(pwd_new);}//to reset the password
    public void numEdit(int index, String num_new) {get(index).setPhoneNumber(num_new);}//to edit the phone number
    public boolean passwordCorrect(int index, String pwd_matching) {return get(index).matchPassword(pwd_matching);}//to match the password of an account
    //to check the legality of a userID
    public boolean uidCheck(String uid_checking) {
        if (uid_checking.length()!=10) {
            return false;
        }
        for (int i=0;i<10;i++) {
            char cur=uid_checking.charAt(i);
            if (!(cur>='0'&&cur<='9')) {
                return false;
            }
        }
        return true;
    }
    //to match a given userID in the account list
    public int uidMatch(String uid_matching) {
        int index=-1;
        int size=listSize();
        boolean found=false;
        for (int i=0;i<size;i++) {
            String cur=get(i).getUID();
            if (cur.indexOf(uid_matching)!=-1) {
                found=true;
                index=i;
                break;
            }
        }
        return index;
    }
    //to create a new legal uid
    public String uidCreate() {
        char[] uid_new=new char[15];
        String uid=new String(uid_new);
        do {
            for (int i=0;i<10;i++) {
                uid_new[i]=(char)(Math.random()*10+'0');
            }
            uid=new String(uid_new);
        } while (uidMatch(uid)!=-1);
        return uid;
    }
    //to check the legality of an identityID
    public boolean ididCheck(String idid_checking) {
        if (idid_checking.length()!=12) {
            return false;
        }
        for (int i=0;i<12;i++) {
            char cur=idid_checking.charAt(i);
            if (!(cur>='0'&&cur<='9')) {
                return false;
            }
        }
        return true;
    }
    //to check the legality of a phone number
    public boolean numCheck(String num_checking) {
        if (num_checking.length()!=11) {
            return false;
        }
        for (int i=0;i<11;i++) {
            char cur=num_checking.charAt(i);
            if (!(cur>='0'&&cur<='9')) {
                return false;
            }
        }
        return true;
    }
    //to check the legality of the format of date
    public boolean birthdayCheck(String birthday_checking) {
        if (birthday_checking.length()!=10) {
            return false;
        }
        if (birthday_checking.charAt(4)!='-'||birthday_checking.charAt(7)!='-') {
            return false;
        }
        for (int i=0;i<10;i++) {
            if (i==4||i==7) {
                continue;
            }
            char cur=birthday_checking.charAt(i);
            if (!(cur>='0'&&cur<='9')) {
                return false;
            }
        }
        return true;
    }
    //to get the difference between two dates
    public long dateDifference(Date start, Date end) {
        long diff=end.getTime()-start.getTime();
        return diff/(24L*60L*60L*1000L*365L);
    }
    //to withdraw a certain amount of money from a certain account
    public boolean withdraw(int index, double amount) {
        Account cur=get(index);
        if (cur.getBalance()<amount) {
            return false;
        }
        cur.setBalance(cur.getBalance()-amount);
        return true;
    }
    //to save a certain amount of money to a certain account
    public void deposit(int index, double amount) {
        Account cur=get(index);
        cur.setBalance(cur.getBalance()+amount);
    }
    //to transfer a certain amount of money from an account to another
    public boolean transfer(int index_from, int index_to, double amount) {
        Account from=get(index_from);
        if (from.getBalance()<amount) {
            return false;
        }
        from.setBalance(from.getBalance()-amount);
        Account to=get(index_to);
        to.setBalance(to.getBalance()+amount);
        return true;
    }
}

public class Body {
    public  StringBuffer strtmp=new StringBuffer();
    private static Bank list=new Bank();
    private static Scanner scanner=new Scanner(System.in);
    private static void Menu() {
        System.out.println("*********************************");
        System.out.println("    1-----Open an account");
        System.out.println("    2-----Cancel an account");
        System.out.println("    3-----Check the balance");
        System.out.println("    4-----Withdraw");
        System.out.println("    5-----Deposit");
        System.out.println("    6-----Transfer");
        System.out.println("    7-----Edit information");
        System.out.println("    11----Exit");
        System.out.println("*********************************");
    }
    public String AccountOpen(String nametmp,String passtmp,String ididtmp,String phonetmp,String sextmp,String bdtmp) {
        String name=nametmp;
        boolean isTrue=true;
        if (name.length()>10) {
            strtmp.append("Error: Name too long!"+"\n"); isTrue=false;
        }
		boolean flag=false;
		for(int i=0;i<name.length();i++){
			if(name.charAt(i)<'0'||name.charAt(i)>'9')
				flag=true;
		}
		if(!flag){isTrue=false;  strtmp.append("Error: Name all numbers!"+"\n");}
        //System.out.print("Create a password: ");
        String password=passtmp;
        if (password.length()<4) {
            strtmp.append("Error: Password too short!"+"\n"); isTrue=false;
        }

        //System.out.print("identityID: ");
        String idid=ididtmp;
        if (!list.ididCheck(idid)) {
            strtmp.append("Error: Illegal identityID!"+"\n"); isTrue=false;
        }

        //System.out.print("Phone number: ");
        String phone_number=phonetmp;
        if (!list.numCheck(phone_number)) {
            strtmp.append("Error: Illegal phone number!"+"\n"); isTrue=false;
        }
		if(phonetmp.charAt(0)=='0'){
		  strtmp.append("Error: Illegal phone number!"+"\n"); isTrue=false;
		}

        //System.out.print("Sex(M/F): ");
        char sex=sextmp.charAt(0);
        if (sex!='M'&&sex!='m'&&sex!='F'&&sex!='f') {
            strtmp.append("Error: Unidentified sex!"+"\n"); isTrue=false;
        }
        if (sex=='m') {
            sex='M';
        }
        if (sex=='f') {
            sex='F';
        }

        //System.out.print("Date of birth(YYYY-MM-DD): ");
        String bd=bdtmp;
        if (!list.birthdayCheck(bd)) {
            strtmp.append("Error: Illegal date of birth!"+"\n"); isTrue=false;
        }
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		format.setLenient(false);
        Date birthday=new Date(0);
        try {
            birthday=format.parse(bd);
        }
        catch (Exception e) {
            strtmp.append("Error: Illegal date of birth!");
            isTrue=false;
        }
        Date now=new Date();
        long dif=list.dateDifference(birthday,now);
        if (dif<18) {
            strtmp.append("Error: Under 18!");
            isTrue=false;
        }
        if (dif>70) {
            strtmp.append("Error: Above 70!");
            isTrue=false;
        }


        String uid=list.uidCreate();
        if(isTrue){
            list.add(new Account(uid,name,password,idid,phone_number,sex,birthday));
            strtmp.append("Success! Your userID: " + uid+"\n");
            System.out.println("Success! Your userID: " + uid+"\n");
            return uid;
        }
        return null;
    }
    //还未做修改
    public void AccountCancelation(String uidtmp) {

        String deleting=uidtmp;
        if (!list.uidCheck(deleting)) {
            System.out.println("Error: Illegal userID!");
            return ;
        }
        int index=list.uidMatch(deleting);
        if (index==-1) {
            System.out.println("Error: User not found!");
        } else {
            System.out.print("Enter the password: ");
            if (!list.passwordCorrect(index,scanner.next())) {
                System.out.println("Error: Password incorrect!");
                return ;
            }

            list.delete(index);
            System.out.println("Success!");
        }
    }
    private static void BalanceCheck() {
        System.out.print("userID: ");
        String enquiring=scanner.next();
        if (!list.uidCheck(enquiring)) {
            System.out.println("Error: Illegal userID!");
            return ;
        }

        int index=list.uidMatch(enquiring);
        if (index==-1) {
            System.out.println("Error: User not found!");
        } else {
            System.out.print("Enter the password: ");
            if (!list.passwordCorrect(index,scanner.next())) {
                System.out.println("Error: Password incorrect!");
                return ;
            }

            System.out.print("Balance of user " + enquiring);
            System.out.printf(": %.2f\n",list.balanceQuery(index));
        }
    }
    private static void Withdraw() {
        System.out.print("userID: ");
        String withdrawing_from=scanner.next();
        if (!list.uidCheck(withdrawing_from)) {
            System.out.println("Error: Illegal userID!");
            return ;
        }

        int index=list.uidMatch(withdrawing_from);
        if (index==-1) {
            System.out.println("Error: User not found!");
        } else {
            System.out.print("Amount: ");
            double amount=0.00;
            if (scanner.hasNextDouble()) {
                amount=scanner.nextDouble();
            } else {
                System.out.println("Error: Illegal type-in!");
                return ;
            }

            System.out.print("Enter the password: ");
            if (!list.passwordCorrect(index,scanner.next())) {
                System.out.println("Error: Password incorrect!");
                return ;
            }

            if (list.withdraw(index,amount)) {
                System.out.println("Success!");
            } else {
                System.out.println("Error: Balance not enough!");
            }
        }
    }
    private static void Deposit() {
        System.out.print("userID: ");
        String depositing_to=scanner.next();
        if (!list.uidCheck(depositing_to)) {
            System.out.println("Error: Illegal userID!");
            return ;
        }

        int index=list.uidMatch(depositing_to);
        if (index==-1) {
            System.out.println("Error: User not found!");
        } else {
            System.out.print("Amount: ");
            double amount=0.00;
            if (scanner.hasNextDouble()) {
                amount=scanner.nextDouble();
            } else {
                System.out.println("Error: Illegal type-in!");
                return ;
            }

            System.out.print("Enter the password: ");
            if (!list.passwordCorrect(index,scanner.next())) {
                System.out.println("Error: Password incorrect!");
                return ;
            }

            list.deposit(index,amount);
            System.out.println("Success!");
        }
    }
    private static void Transfer() {
        System.out.print("Transfer from (userID): ");
        String from=scanner.next();
        if (!list.uidCheck(from)) {
            System.out.println("Error: Illegal userID!");
            return ;
        }
        int index_from=list.uidMatch(from);
        if (index_from==-1) {
            System.out.println("Error: User not found!");
            return ;
        }

        System.out.print("Transfer to (userID): ");
        String to=scanner.next();
        if (!list.uidCheck(to)) {
            System.out.println("Error: Illegal userID!");
            return ;
        }
        int index_to=list.uidMatch(to);
        if (index_to==-1) {
            System.out.println("Error: User not found!");
            return ;
        }

        System.out.print("Amount: ");
        double amount=0.00;
        if (scanner.hasNextDouble()) {
            amount=scanner.nextDouble();
        } else {
            System.out.println("Error: Illegal type-in!");
            return ;
        }

        System.out.print("Enter the password: ");
        if (!list.passwordCorrect(index_from,scanner.next())) {
            System.out.println("Error: Password incorrect!");
            return ;
        }

        if (list.transfer(index_from,index_to,amount)) {
            System.out.println("Success!");
        } else {
            System.out.println("Error: Balance not enough!");
        }
    }
    public static void NameEdit(int index) {
        System.out.print("New name: ");
        String name_new=scanner.next();
        list.nameEdit(index,name_new);
        System.out.println("Success!");
    }
    public static void PhoneNumberEdit(int index) {
        System.out.print("New phone number: ");
        String num_new=scanner.next();
        if (!list.numCheck(num_new)) {
            System.out.println("Error: Illegal phone number!");
        } else {
            list.numEdit(index,num_new);
            System.out.println("Success!");
        }
    }
    public static boolean PasswordReset(int index) {
        System.out.print("New password: ");
        String pwd_new=scanner.next();
        if (pwd_new.length()<4) {
            return false;
        } else {
            list.passwordEdit(index,pwd_new);
            return true;
        }
    }
    public static void InformationEdition() {
        System.out.print("userID: ");
        String editing=scanner.next();
        if (!list.uidCheck(editing)) {
            System.out.println("Error: Illegal userID!");
            return ;
        }

        int index=list.uidMatch(editing);
        if (index==-1) {
            System.out.println("Error: User not found!");
        } else {
            System.out.println("1:Name 2:PhoneNumber 3:Password");
            System.out.print("Choose the item to edit(1/2/3): ");
            int op;
            if (scanner.hasNextInt()) {
                op=scanner.nextInt();
            } else {
                System.out.println("Error: Please enter an integer!");
                return ;
            }

            System.out.print("Enter the password: ");
            if (!list.passwordCorrect(index,scanner.next())) {
                System.out.println("Error: Password incorrect!");
                return ;
            }

            switch (op) {
                case 1: //to edit the name
                    NameEdit(index);
                    break;
                case 2: //to edit the phone number
                    PhoneNumberEdit(index);
                    break;
                case 3:	//to reset the password
                    PasswordReset(index);
                    break;
                default: System.out.println("Error: Please enter an integer from 1 to 3!");
            }
        }
    }
}
