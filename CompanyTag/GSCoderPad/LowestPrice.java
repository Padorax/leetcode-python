package CompanyTag.GSCoderPad;

public class LowestPrice {
    private record Interval (int startTime, int endTime, int price) {
        public Interval {
            if (startTime >= endTime) {
                throw new IllegalArgumentException("aaa");
            } else if (startTime < 0 || endTime < 0 || price < 0) {
                throw new IllegalArgumentException("bbb");
            }
        }
    }

    private static class BST {
        private final Interval data;
    }

}