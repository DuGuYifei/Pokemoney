# execute transction_auto.sh per month

```bash
crontab -e
# add the following line
0 0 25 * * /root/phoenix-sql/transaction_auto.sh
```