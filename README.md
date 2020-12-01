# vcfstats
BGI-lowpass imputation vcf stats

### usage
```
usage: java -jar vcfstats.jar [-d <FILE>] [-h] -i <FILE> [-o <String>] [-q <Float>]

Options:
 -d,--dbsnp <FILE>      dbsnp file(.vcf.gz), must be indexed [null]
 -h,--help              Print this help.
 -i,--input <FILE>      input file(VCF). [request]
 -o,--output <String>   report outdir [request]
 ```

### 统计指标说明

| 指标 | 说明 |
| --- | --- |
Failed Filters | 未通过质控的变异条目数
Passed Filters | 通过质控的变异条目数
SNPs | SNP类型变异条目数
Insertions | Insertion类型变异条目数
Deletions | Deletion类型变异条目数
Same as reference | 未变异条目数(homref)
Missing Genotype | 缺失genotype信息(因质量过低过滤掉)
SNP Transitions/Transversions | 转换颠换比
Total Het/Hom ratio | 杂合纯合比
SNP Het/Hom ratio | SNP类型杂合纯合比
Insertion Het/Hom ratio | Insertion类型杂合纯合比
Deletion Het/Hom ratio | Deletion类型杂合纯合比
Insertion/Deletion ratio | Insertion类型变异和Deletion类型变异比例
Indel/SNP ratio | Indel和SNP类型变异比例
max_GP >=0.8 ratio | GP值>=0.8的变异条目比例
max_GP >=0.9 ratio | GP值>=0.9的变异条目比例
