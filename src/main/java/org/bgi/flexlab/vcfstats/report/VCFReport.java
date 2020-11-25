/*******************************************************************************
 * Copyright (c) 2017, BGI-Shenzhen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *******************************************************************************/
package org.bgi.flexlab.vcfstats.report;

import htsjdk.samtools.util.CloseableIterator;
import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.Genotype;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.variantcontext.VariantContextBuilder;
import htsjdk.variant.vcf.VCFFileReader;
import org.bgi.flexlab.vcfstats.Options;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class VCFReport {

    final private static String REPORT_HEADER = "## BGI-lowpass imputation vcf stats, version 1.0\n";

    private Map<String, SampleVCFReport> perSampleVCFReports;
    private Options options;
    private VCFFileReader vcfReader;

    public VCFReport(Options options){
        perSampleVCFReports = new HashMap<>();
        this.options = options;
        if(options.getDbsnp() != null)
            vcfReader = new VCFFileReader(new File(options.getDbsnp()));
    }

    public void stats(){
        VCFFileReader reader = new VCFFileReader(new File(options.getInfile()), false);

        CloseableIterator<VariantContext> it = reader.iterator();
        while (it.hasNext()) {
            VariantContext vc = it.next();
            parseVariation(vc);
        }
    }

    public void parseVariation(VariantContext vc){

        if(options.getDbsnp() != null)
            vc = setDbSNP(vc);

        SampleVCFReport sampleReport;
        for(String sample: vc.getSampleNames()){
            Genotype gt = vc.getGenotype(sample);

            if(perSampleVCFReports.containsKey(sample))
                sampleReport = perSampleVCFReports.get(sample);
            else {
                sampleReport = new SampleVCFReport(options.isCountVarLength());
                perSampleVCFReports.put(sample, sampleReport);
            }

            sampleReport.add(vc, sample);
        }
    }

    private VariantContext setDbSNP(VariantContext vc) {
        CloseableIterator<VariantContext> vcfIter = vcfReader.query(vc.getContig(), vc.getStart(), vc.getEnd());

        String id = null;
        while (vcfIter.hasNext()) {
            VariantContext dbsnpvc = vcfIter.next();
            if (dbsnpvc.getStart() != vc.getStart() || dbsnpvc.getEnd() != vc.getEnd())
                continue;

            for(Allele allele: vc.getAlternateAlleles()){
                for(Allele dnsnpAllele: dbsnpvc.getAlternateAlleles()) {
                    if(allele.equals(dnsnpAllele)) {
                        id = dbsnpvc.getID();
                        break;
                    }
                }
                if(id != null)
                    break;
            }
            if(id != null)
                break;
        }

        if(id != null) {
            return new VariantContextBuilder(vc).id(id).make();
        }
        return vc;
    }

    public void writeReport() throws IOException {
        for(String sample: perSampleVCFReports.keySet()){
            File report = new File(options.getOutdir(), sample + ".vcfstats.report.txt");
            FileWriter fileWritter = new FileWriter(report,false);
            fileWritter.write(REPORT_HEADER);
            fileWritter.write(perSampleVCFReports.get(sample).getReport());
            fileWritter.close();
        }
    }
}
