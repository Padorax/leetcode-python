package CompanyTag.GSCoderPad;

import java.util.Map;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;

public class LargestTree {
    public static int largestTree(final Map<Integer, Integer> immediateParent) {
        int maxSize = 0;
        int minRootId = 0;
        HashSet<Integer> rootIds = new HashSet<>();
        Map<Integer, LinkedList<Integer>> parentToChild = new HashMap<>();

        for (Map.Entry<Integer, Integer> entry : immediateParent.entrySet()) {
            int child = entry.getKey();
            int parent = entry.getValue();
            parentToChild.putIfAbsent(parent, new LinkedList<>());
            parentToChild.get(parent).add(child); //<- don't get parent-child wrong!

            if (!immediateParent.containsKey(parent)) {
                rootIds.add(parent);
            }
        }

        for (int rootId : rootIds) {
            int curSize = getTreeSize(rootId, parentToChild);
            if (curSize > maxSize) {
                maxSize = curSize;
                minRootId = rootId;
            } else if (curSize == maxSize) {
                minRootId = Math.min(minRootId, rootId);
            }
        }

        return maxSize;
    }

    private static int getTreeSize(int rootId, Map<Integer, LinkedList<Integer>> parentToChild) {
        int size = 0;
        LinkedList<Integer> queue = new LinkedList<>(Arrays.asList(rootId));
        while (!queue.isEmpty()) {
            int node = queue.removeFirst();
            size++;

            if (parentToChild.containsKey(node)) {
                for (int child : parentToChild.get(node)) {
                    queue.addLast(child);
                }
            }
        }
        return size;
    }
}