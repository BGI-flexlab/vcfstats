package org.bgi.flexlab.vcfstats;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Options {

    final String SOFTWARE_NAME = "vcfstats";
    private boolean countVarLength = false;
    private String infile;
    private String outfile = null;
    private String dbsnp;

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    String compile_date = df.format(new Date());

    org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();
    CommandLine cmdLine;
    CommandLineParser parser = new DefaultParser();

    public Options() {
    }

    public Options(String[] args) {
        parse(args);
    }

    private String helpHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nVersion     : ");
        sb.append(getAppVersion());
        sb.append("\nAuthor      : huangzhibo@genomics.cn");
        sb.append("\nCompile Date: ");
        sb.append(compile_date);
        sb.append("\nNote        : BGI-lowpass imputation vcf stats\n");
        sb.append("\nOptions:\n");
        return sb.toString();
    }

    public void parse(String[] args) {
        String header = helpHeader();
        String footer = "\nPlease report issues at https://github.com/BGI-flexlab/vcfstats/issues";

        options.addOption(Option.builder("i")
                .longOpt("input")
                .required(true)
                .hasArg()
                .argName("FILE")
                .desc("input file(VCF). [request]")
                .build());
        options.addOption(Option.builder("o")
                .longOpt("output")
                .hasArg()
                .argName("String")
                .desc("report outfile [request]")
                .build());
        options.addOption(Option.builder("d")
                .longOpt("dbsnp")
                .hasArg()
                .argName("FILE")
                .desc("dbsnp file(.vcf.gz), must be indexed [null]")
                .build());
        options.addOption(Option.builder("a")
                .longOpt("allele-lengths")
                .desc("output variant length histogram [false]")
                .build());
        options.addOption(Option.builder("h")
                .longOpt("help")
                .desc("Print this help.")
                .build());

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(2 * HelpFormatter.DEFAULT_WIDTH);

        try {
            cmdLine = parser.parse(options, args);
            if (cmdLine.hasOption("h")) {
                formatter.printHelp("java -jar " + SOFTWARE_NAME + ".jar", header, options, footer, true);
                System.exit(0);
            }
        } catch (ParseException e) {
            formatter.printHelp("java -jar " + SOFTWARE_NAME + ".jar", header, options, footer, true);
            System.exit(0);
        }

        infile = cmdLine.getOptionValue("input");

        if (cmdLine.hasOption("output")) {
            outfile = cmdLine.getOptionValue("output");
        }

        if (cmdLine.hasOption("dbsnp")) {
            dbsnp = cmdLine.getOptionValue("dbsnp");
        }

        if (cmdLine.hasOption("allele-lengths")) {
            countVarLength = cmdLine.hasOption("allele-lengths");
        }
    }

    public String getInfile() {
        return infile;
    }

    public String getOutfile() {
        return outfile;
    }

    public String getDbsnp() {
        return dbsnp;
    }

    public boolean isCountVarLength() {
        return countVarLength;
    }

    public static String getAppVersion() {
        String appVersion = "NA";
        Properties properties = new Properties();
        try {
            properties.load(Options.class.getClassLoader().getResourceAsStream("app.properties"));
            if (!properties.isEmpty()) {
                appVersion = properties.getProperty("app.version");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appVersion;
    }
}

