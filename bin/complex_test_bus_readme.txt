>complex_test_bus.txt file tests for the following commands:
>    add_bus
>    add_stop
>    add_route
>    extend_route
>    add_depot
>    speed_limit
>    path_delay
>    stop_down
>    bus_down
>    add_event for move_bus
>    fuel_report
>    quit
>THE FILE DOES NOT TEST FOR THE BEHAVIOR OF BUS REFUEL

add_stop,0,Appleville,5,0.0,0.08
add_stop,1,Banana Bayou,0,0.04,0.1
add_stop,2,Star City,0,0.08,0.16
add_stop,3,Cherry City,0,0.16,0.1
add_stop,4,West Side,0,0.01,0.01
add_stop,5,South Side,0,0.04,0.01
add_stop,6,East Side,0,0.1,0.02
add_stop,7,Central City,0,0.04,0.05
add_depot,90,North Depot,0.03,0.16 //included the add_depot command
add_route,0,10,Express
add_route,1,16,Perimeter
extend_route,0,5
extend_route,0,7
extend_route,0,1
extend_route,0,2
extend_route,0,1
extend_route,0,7
extend_route,1,4
extend_route,1,0
extend_route,1,1
extend_route,1,2
extend_route,1,3
extend_route,1,6
extend_route,1,5
speed_limit,5,15,6,7,5 //no effect
path_delay,90,10,0,3,5.0 //no effect
speed_limit,10,20,2,1,35 //delay next move_bus event for busID:7 by 3 timeRank units
path_delay,12,20,2,1,1.5 //delay next move_bus event for busID:7 by 5 timeRank units (overlaps with speed_limit)
path_delay,25,50,2,3,2.0 //delay next move_bus event for busID:11 by 15 timeRank units
stop_down,70,30,6 //passengers on busID:11 should remain at 4 ("post-stop: 4") for event at timeRank:72
speed_limit,70,30,6,5,15 //delay next move_bus event for bus ID:11 by 9 timeRank units
add_bus,7,0,0,0,10,500,500,50 //modified input to include fuel and fuel capacity attributes
add_bus,11,1,0,0,10,10000,10000,30 //modified input to include fuel and fuel capacity attributes
bus_down,39,7,30,10 //delay next move_bus event for busID:7 by (40 + time from depot to the first stop) timeRank units and "post-stop: 0" for event at timeRank:40
add_event,1,move_bus,7
add_event,1,move_bus,11
step_multi,200
system_report
fuel_report //included the fuel_report command
display_model
quit
