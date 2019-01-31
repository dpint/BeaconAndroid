package dpint.si.app;

class Utils {

    static Integer tryParseInt(String number) {
        try{
            return Integer.parseInt(number);
        }catch (NumberFormatException ignored){
            return null;
        }
    }
}
