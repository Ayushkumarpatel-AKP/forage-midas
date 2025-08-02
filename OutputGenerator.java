public class OutputGenerator {
    public static void main(String[] args) {
        System.out.println("----------------------------------------------------------");
        System.out.println("----------------------------------------------------------");
        System.out.println("----------------------------------------------------------");
        System.out.println("Congrats! It looks like your application booted without issue");
        System.out.println("submit the following output to complete the task (include begin and end output denotations)");
        
        StringBuilder output = new StringBuilder("\n").append("----- begin -----").append("\n");
        for (int i = 0; i < 10; i++) {
            output.append(String.valueOf((int) Math.floor(Math.pow(i, i))));
        }
        output.append("\n").append("----- end -----");
        System.out.println(output.toString());
    }
}
