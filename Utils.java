class Utils {
    /**
     * Parses an array of strings into an array of doubles.
     * @param numberStrings Array of strings representing numbers
     * @return Array of doubles parsed from the strings
     */
    static double[] parseDoubles(String[] numberStrings) {
        double[] numbers = new double[numberStrings.length];
        for (int i = 0; i < numberStrings.length; i++) {
            numbers[i] = Double.parseDouble(numberStrings[i].trim());
        }
        return numbers;
    }
}
