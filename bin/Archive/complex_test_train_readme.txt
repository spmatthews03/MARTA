>complex_test_train.txt file tests for the following commands:
>    add_train
>    add_station
>    add_train_route
>    extend_train_route
>    add_depot
>    train_speed_limit
>    train_path_delay
>    station_down
>    train_down
>    add_event for move_train
>    quit

add_station,0,Appleville Station,5,0.0,0.08
add_station,1,Banana Bayou Station,0,0.04,0.1
add_station,2,Star City Station,0,0.08,0.16
add_station,3,Cherry City Station,0,0.16,0.1
add_station,4,West Station,0,0.01,0.01
add_station,5,South Station,0,0.04,0.01
add_station,6,East Station,0,0.1,0.02
add_station,7,Central City Station,0,0.04,0.05
add_depot,90,North Depot,0.03,0.16 //included the add_depot command
add_train_route,0,90,Northbound
add_train_route,1,60,Westbound
extend_train_route,0,5
extend_train_route,0,7
extend_train_route,0,1
extend_train_route,0,2
extend_train_route,0,1
extend_train_route,0,7
extend_train_route,1,4
extend_train_route,1,0
extend_train_route,1,1
extend_train_route,1,2
extend_train_route,1,3
extend_train_route,1,6
extend_train_route,1,5
train_speed_limit,5,15,6,7,1 //no effect
train_path_delay,90,10,0,3,100.0 //no effect
train_speed_limit,10,20,2,1,10 //delay next move_train event for railcarID:7 by 28 timeRank units
train_path_delay,12,20,2,1,1.2 //delay next move_train event for railcarID:7 by 7 timeRank units (overlaps with train_speed_limit)
train_path_delay,25,50,2,3,3.0 //delay next move_train event for railcarID:11 by 30 timeRank units
station_down,85,30,6 //passengers on railcarID:11 should remain at 4 ("post-stop: 4") for event at timeRank:87
train_speed_limit,85,30,6,5,15 //delay next move_train event for bus ID:11 by 9 timeRank units
add_train,7,0,0,0,10,50
add_train,11,1,0,0,10,30
add_train,20,0,0,0,10,50
train_down,66,7,30,10 //delay next move_train event for railcarID:7 by (40 + time from depot to the first stop) timeRank units and "post-stop: 0" for event at timeRank:67. This should also delay railcarID:20
add_event,1,move_train,7
add_event,1,move_train,11
add_event,5,move_train,20
step_multi,200
system_report
display_model
quit