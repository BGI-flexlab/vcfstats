# vcfstats
BGI-lowpass imputation vcf stats

```
usage: java -jar vcfstats.jar [-d <FILE>] [-h] -i <FILE> [-o <String>] [-q <Float>]

Options:
 -d,--dbsnp <FILE>      dbsnp file(.vcf.gz), must be indexed [null]
 -h,--help              Print this help.
 -i,--input <FILE>      input file(VCF). [request]
 -o,--output <String>   report outdir [request]
 -q,--qual <Float>      the minimum phred-scaled confidence threshold at which variants should be counted [0]
 ```
