package repository.gw.enums;

import java.util.HashMap;
import java.util.Map;

public enum ClaimsUsers {
    ktennant("Ken Tennant", "Claims Admin"),
    caverett("Cory Averett", "OCR"),
    emacdonald("Elly MacDonald", "Examiner"),
    kcooper("KaLynn Cooper", "Adjuster"),
    abatts("Ashley Batts", "Manager"),
    adeuell("Annie Deuell", "Adjuster"),
    ajones("Angie Jones", "Manager"),
    astefanic("Angela Stefanic", "Adjuster"),
    atubb("Amber Tubb", "Adjuster"),
    bhogan("Brian Hogan", "Adjuster"),
    bspeakman("Brenda Speakman", "Adjuster"),
    cdegn("Christina Degn", "Examiner"),
    cgonzalez("Carina Gonzalez", "Examiner"),
    cKnapp("Curt Knapp", "Adjuster"),
    cwilliams("Chad Williams", "Adjuster"),
    dalley("Doug Alley", "Manager"),
    ddana("Drue Dana", "Examiner"),
    dfreter("Darrell Freter", "Adjuster"),
    dhobbs("Debbie Hobbs", "Adjuster"),
    dhuebert("Darin Huebert", "Adjuster"),
    dkessel("David Kessel", "Adjuster"),
    dpatterson("Dana Patterson", "Adjuster"),
    drogers("Doyle Rogers", "Adjuster"),
    gmurray("Greg Murray", "Adjuster"),
    hbartschi("Holly Bartschi", "Adjuster"),
    jallen("Jamie Allen", "Adjuster"),
    jgross("Janna Gross", "Examiner"),
    jhirschi("Jaclyn Hirschi", "Adjuster"),
    jkerfoot("Jill Kerfoot", "OCR"),
    jmower("James Mower", "Adjuster"),
    jneilandersen("Jerry Neil-Andersen", "Adjuster"),
    jreid("Jon Reid", "Manager"),
    jsmith("Jessica Smith", "OCR"),
    kdurfee("Kevin Durfee", "OCR"),
    klonghurst("Kristen Longhurst", "Adjuster"),
    kwatson("Katie Watson", "OCR"),
    adegiulio("Lexcie DeGiulio", "Adjuster"),
    lhopster("Lorie Hopster", "OCR"),
    ltolman("Lorene Tolman", "Adjuster"),
    manelson("Melissa Nelson", "OCR"),
    msouza("Michelle Souza", "Adjuster"),
    nlemmon("Natalie Lemmon", "OCR"),
    pschiffman("Paul Schiffman", "Adjuster"),
    rburgoyne("Rich Burgoyne", "Claims VP"),
    rkivi("Roy Kivi", "Adjuster"),
    rlauson("Richard Lauson", "Adjuster"),
    rmaxwell("Rita Maxwell", "Examiner"),
    scondie("Sherri Condie", "OCR"),
    sdavis("Shellie Davis", "Adjuster"),
    sjohnson("Steve Johnson", "Manager"),
    sschlottman("Steve Schlottman", "Adjuster"),
    swerth("Sarah Werth", "Examiner"),
    dallen("Devanee Allen", "Examiner"),
    tcutler("Tina Cutler", "Adjuster"),
    tdurrant("Teresa Durrant", "Adjuster"),
    tmakinson("Trevor Makinson", "Adjuster"),
    tpincock("Tyler Pincock", "Adjuster"),
    tpowers("Thomas Powers", "Adjuster"),
    tstrouse("Time Strouse", "Adjuster"),
    vkinter("Vicki Kinter", "Examiner"),
    lbarber("Lisa Barber", "Underwriter"),
    pdye("Paula Dye", "Underwriter"),
    panderson("Patty Anderson", "Underwriter"),
    tsenn("Tina Senn", "Examiner"),
    srodriguez("Salvador Rodriguez", "Adjuster"),
    swestfall("Steve Westfall", "OCR"),
    msiharath("Michelle Siharath", "Examiner"),
    nspangler("Nanette Spangler", "OCR"),
    bbischoff("Breanna Bischoff", "Accounting"),
    mchau("Michele Chau", "Examiner");

    private String name;
    private String role;
    private  String password;
    private static final Map<String, ClaimsUsers> USERS_BY_ROLE = new HashMap<>();

    static {
        for (ClaimsUsers user : values()) {
            USERS_BY_ROLE.put(user.getRole(), user);
        }
    }

    ClaimsUsers(String name, String role) {
        this.name = name;
        this.role = role;
        this.password = "gw";
    }

    ClaimsUsers(String name, String role, String password) {
        this.name = name;
        this.role = role;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public static ClaimsUsers valueOfRole(final String role) {
        final ClaimsUsers user = USERS_BY_ROLE.get(role);
        if (user != null) {
            return user;
        } else {
            return null;
        }
    }

    public static ClaimsUsers valueOfName(final String name) {
        try {
            return valueOf(name);
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }

/*    public static ClaimsUsers valueOfPassword(final String password) {
        try {
            return valueOf(password);
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }*/

    public static String getEnumByString(String fullName) {
        for (ClaimsUsers e : ClaimsUsers.values()) {
            if (fullName.equalsIgnoreCase(e.name)) {
                return e.name();
            }
        }
        return null;
    }

}
    
    