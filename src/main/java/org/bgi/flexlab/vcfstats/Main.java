package org.bgi.flexlab.vcfstats;

import htsjdk.variant.variantcontext.VariantContext;
import org.bgi.flexlab.vcfstats.report.VCFReport;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Options options = new Options(args);
        VCFReport report = new VCFReport(options);
        report.stats();
        try {
            report.writeReport();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
