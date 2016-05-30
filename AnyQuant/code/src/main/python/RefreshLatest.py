# coding=utf-8
import tushare as ts
from dateutil import rrule
import datetime
import os


# get workday numbers between start and end
def getWorkdaysNumbers(start, end, holidays=0, days_off=None):
    if days_off is None:
        days_off = 5, 6
    workdays = [x for x in range(7) if x not in days_off]
    days = rrule.rrule(rrule.DAILY, dtstart=start, until=end, byweekday=workdays)
    return days.count() - holidays

# get workday
count = 0
today = datetime.date.today()
deltadays = datetime.timedelta(days=1)
yesterday = today - deltadays
# 设置输出格式
ISOFORMAT = '%Y-%m-%d'
today.strftime(ISOFORMAT)

# check today
str_today = str(today)
int_year = int(str_today[0:4])
int_month = int(str_today[5:7])
int_day = int(str_today[8:10])
workDayNum = getWorkdaysNumbers(datetime.date(int_year, int_month, int_day),
                                datetime.date(int_year, int_month, int_day), 0)

latestWorkday = today
today_is_workDay = workDayNum > 0

while workDayNum == 0:
    # check yesterday
    latestWorkday.strftime(ISOFORMAT)
    str_latestWorkday = str(latestWorkday)
    yesterday = latestWorkday - deltadays
    int_year = int(str_latestWorkday[0:4])
    int_month = int(str_latestWorkday[5:7])
    int_day = int(str_latestWorkday[8:10])
    workDayNum = getWorkdaysNumbers(datetime.date(int_year, int_month, int_day),
                                    datetime.date(int_year, int_month, int_day), 0)
    latestWorkday = yesterday

# get latest workday
print today_is_workDay
if today_is_workDay :
    latestWorkday = today
else:
    latestWorkday = latestWorkday + deltadays
latestWorkday.strftime(ISOFORMAT)

print 'latestWorkday:', latestWorkday

# read selected stocks' number
number = []
number_with_ex = []
file_object = open('code/cache/selectedStocks.txt')
for line in file_object:
    if len(line) > 12:
        number.append(line[2:8])
        number_with_ex.append(line[0:8])
file_object.close()

dirs = 'code/cache/latestData/' + latestWorkday.strftime(ISOFORMAT)
if not os.path.exists(dirs):
    os.mkdir(dirs)

# download latest workday's info
for code in number:
    print 'download:', number_with_ex[count]
    df = ts.get_tick_data(code, latestWorkday)
    df.to_csv(dirs+'/' + number_with_ex[count] + '.txt')
    count += 1
print 'download latest info done !'
