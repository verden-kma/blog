package edu.ukma.blog.utils;

import java.io.File;
import java.util.Arrays;

public class VarLenRWayTree {
    private static final String ALPHAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String RADIX = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ_";

    /**
     * creates R-way-like structure of directories, with first layer consisting of <code>alphas</code> and
     * the following - <code>radix</code>
     * @param depth - number of layers in the tree
     * @param basePath - root directory
     */
    public static void build(int depth, String pathTemplate, final File basePath) {
        final Character[] pathArgs = new Character[depth]; // hint to compiler to optimise 'length - 1'
        Arrays.fill(pathArgs, RADIX.charAt(0));
        final int[] pathArgsIndices = new int[depth];

        for (int i = 0, currInd = depth - 1; i < ALPHAS.length(); i++, currInd = depth - 1) {
            pathArgs[0] = ALPHAS.charAt(i);
            new File(basePath, String.format(pathTemplate, (Object[]) pathArgs)).mkdirs();

            while (currInd > 0) {
                if (pathArgsIndices[currInd] == RADIX.length() - 1) {
                    pathArgs[currInd] = RADIX.charAt(pathArgsIndices[currInd] = 0);

                    currInd--;
                    continue;
                }
                pathArgs[currInd] = RADIX.charAt(++pathArgsIndices[currInd]);
                currInd = pathArgs.length - 1;

                new File(basePath, String.format(pathTemplate, (Object[]) pathArgs)).mkdirs();
            }
        }
    }
}
