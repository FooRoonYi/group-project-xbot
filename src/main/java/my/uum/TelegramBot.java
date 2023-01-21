package my.uum;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is to send message and receive message by using BotUsername and BotToken.
 *
 * @author Group X_Bot
 */
public class TelegramBot extends TelegramLongPollingBot {

    SendMessage sendMessage = new SendMessage();

    Map<String, ArrayList<String>> insertUserData = new HashMap<String, ArrayList<String>>(); //register user
    Map<String, ArrayList<String>> updateUserData = new HashMap<String, ArrayList<String>>(); //update user
    Map<String, ArrayList<String>> insertBookingData = new HashMap<String, ArrayList<String>>(); //add booking
    Map<String, ArrayList<String>> insertAdminData = new HashMap<String, ArrayList<String>>(); //register as admin
    Map<String, ArrayList<String>> insertRoomData = new HashMap<String, ArrayList<String>>(); //add room
    Map<String, ArrayList<String>> updateRoomData = new HashMap<String, ArrayList<String>>(); //update room
    Map<String, ArrayList<String>> deleteUserData = new HashMap<String, ArrayList<String>>(); //delete user

    Map<String, String> flow = new HashMap<String, String>();


    DatabaseConnect connect = new DatabaseConnect();

    /**
     * This method is used to get botUsername
     *
     * @return botUsername STIW3054_X_bot
     */
    @Override
    public String getBotUsername() {
        return "STIW3054_X_bot";
    }

    /**
     * This method is used to get botToken
     *
     * @return botToken 5856052356:AAEek4evlR_UfWNGNadv619lEiZJzw6Q3-w
     */
    @Override
    public String getBotToken() {
        return "5856052356:AAEek4evlR_UfWNGNadv619lEiZJzw6Q3-w";
    }

    /**
     * This method is used to send command and receive message from TelegramBot.
     * This program includes the admin and user parts.
     * User: register as user, update user details, book meeting room, cancel booking, view booking list and available room list
     * Admin: register as admin, add room, update room, check booking list
     * Methods in the same class will be called in this method.
     */
    @Override
    public void onUpdateReceived(Update update) {
        String command = update.getMessage().getText();

        if (update.getMessage().hasText()) {
            if (!insertUserData.containsKey(String.valueOf(update.getMessage().getChatId()))) {
                insertUserData.put(String.valueOf(update.getMessage().getChatId()), new ArrayList<String>());
            }
            if (!updateUserData.containsKey(String.valueOf(update.getMessage().getChatId()))) {
                updateUserData.put(String.valueOf(update.getMessage().getChatId()), new ArrayList<String>());
            }
            if (!deleteUserData.containsKey(String.valueOf(update.getMessage().getChatId()))) {
                deleteUserData.put(String.valueOf(update.getMessage().getChatId()), new ArrayList<String>());
            }
            if (!insertBookingData.containsKey(String.valueOf(update.getMessage().getChatId()))) {
                insertBookingData.put(String.valueOf(update.getMessage().getChatId()), new ArrayList<String>());
            }
            if (!insertAdminData.containsKey(String.valueOf(update.getMessage().getChatId()))) {
                insertAdminData.put(String.valueOf(update.getMessage().getChatId()), new ArrayList<String>());
            }
            if (!insertRoomData.containsKey(String.valueOf(update.getMessage().getChatId()))) {
                insertRoomData.put(String.valueOf(update.getMessage().getChatId()), new ArrayList<String>());
            }
            if (!updateRoomData.containsKey(String.valueOf(update.getMessage().getChatId()))) {
                updateRoomData.put(String.valueOf(update.getMessage().getChatId()), new ArrayList<String>());
            }

            //command - start
            if (command.equals("/start")) {
                commandStart(String.valueOf(update.getMessage().getChatId()));
            } else
                //command - registeruser
                if (command.equals("/registeruser")) {
                    commandRegisterUser(String.valueOf(update.getMessage().getChatId()));
                } else
                    //command - updateuser
                    if (command.equals("/updateuser")) {
                        commandUpdateUser(String.valueOf(update.getMessage().getChatId()));
                    } else
                        //command - registeradmin
                        if (command.equals("/registeradmin")) {
                            commandRegisterAdmin(String.valueOf(update.getMessage().getChatId()));
                        } else
                            //command - booking
                            if (command.equals("/booking")) {
                                commandBooking(String.valueOf(update.getMessage().getChatId()));
                            } else
                                //command - cancel
                                if (command.equals("/cancel")) {
                                    commandCancel(String.valueOf(update.getMessage().getChatId()));
                                    flow.put(String.valueOf(update.getMessage().getChatId()), "cancel");
                                } else
                                    //command - bookinglist
                                    if (command.equals("/bookinglist")) {
                                        commandDisplay(String.valueOf(update.getMessage().getChatId()));
                                    } else
                                        //command - roomlist
                                        if (command.equals("/roomlist")) {
                                            commandDisplayAvailbleRoom(String.valueOf(update.getMessage().getChatId()));
                                        } else
                                            //command - addroom
                                            if (command.equals("/addroom")) {
                                                commandAddRoom(String.valueOf(update.getMessage().getChatId()));
                                            } else
                                                //command - updateroom
                                                if (command.equals("/updateroom")) {
                                                    commandUpdateRoom(String.valueOf(update.getMessage().getChatId()));
                                                } else
                                                    //for quit
                                                    if (command.equals("2") || command.equals("0") || command.equals("N") || command.equals("n") || command.equals("4") || command.equals("6") || command.equals("8") || command.equals("10")) {
                                                        commandQuit(String.valueOf(update.getMessage().getChatId()));
                                                    } else
                                                        //after reply 1, let user enter name
                                                        if (command.equals("1")) {
                                                            inputName(String.valueOf(update.getMessage().getChatId()));
                                                            flow.put(String.valueOf(update.getMessage().getChatId()), "name");
                                                        } else
                                                            //after reply y, let user enter room
                                                            if (command.equals("Y") || command.equals("y")) {
                                                                inputRoom(String.valueOf(update.getMessage().getChatId()));
                                                                flow.put(String.valueOf(update.getMessage().getChatId()), "room");
                                                            } else
                                                                //after reply 3, let user registers as admin
                                                                if (command.equals("3")) {
                                                                    inputAdmin(String.valueOf(update.getMessage().getChatId()));
                                                                    flow.put(String.valueOf(update.getMessage().getChatId()), "admin");
                                                                } else
                                                                    //after reply 5, let admin adds room
                                                                    if (command.equals("5")) {
                                                                        addRoom(String.valueOf(update.getMessage().getChatId()));
                                                                        flow.put(String.valueOf(update.getMessage().getChatId()), "addroom");
                                                                    } else
                                                                        //after reply 7, let user update the email
                                                                        if (command.equals("7")) {
                                                                            inputUpdateEmail(String.valueOf(update.getMessage().getChatId()));
                                                                            flow.put(String.valueOf(update.getMessage().getChatId()), "updateEmail");
                                                                        } else
                                                                            //after reply 9, let admin updates room by checking the room id
                                                                            if (command.equals("9")) {
                                                                                updateCheckRoomID(String.valueOf(update.getMessage().getChatId()));
                                                                                flow.put(String.valueOf(update.getMessage().getChatId()), "updateCheckRoomID");
                                                                            } else

                                                                                //start the flow - user
                                                                                //input name
                                                                                if (flow.get(String.valueOf(update.getMessage().getChatId())).equals("name")) {
                                                                                    switch (insertUserData.get(String.valueOf(update.getMessage().getChatId())).size()) {
                                                                                        case 0:
                                                                                            insertUserData.get(String.valueOf(update.getMessage().getChatId())).add(update.getMessage().getText());
                                                                                            break;

                                                                                        default:
                                                                                            insertUserData.get(String.valueOf(update.getMessage().getChatId())).set(0, update.getMessage().getText());
                                                                                            break;
                                                                                    }

                                                                                    inputIC(String.valueOf(update.getMessage().getChatId()));
                                                                                    flow.put(String.valueOf(update.getMessage().getChatId()), "ic");
                                                                                } else
                                                                                    //input ic
                                                                                    if (flow.get(String.valueOf(update.getMessage().getChatId())).equals("ic")) {
                                                                                        switch (insertUserData.get(String.valueOf(update.getMessage().getChatId())).size()) {
                                                                                            case 1:
                                                                                                insertUserData.get(String.valueOf(update.getMessage().getChatId())).add(update.getMessage().getText());
                                                                                                break;

                                                                                            default:
                                                                                                insertUserData.get(String.valueOf(update.getMessage().getChatId())).set(1, update.getMessage().getText());
                                                                                                break;
                                                                                        }

                                                                                        inputStaffID(String.valueOf(update.getMessage().getChatId()));
                                                                                        flow.put(String.valueOf(update.getMessage().getChatId()), "staffid");
                                                                                    } else
                                                                                        //input staffid
                                                                                        if (flow.get(String.valueOf(update.getMessage().getChatId())).equals("staffid")) {
                                                                                            switch (insertUserData.get(String.valueOf(update.getMessage().getChatId())).size()) {
                                                                                                case 2:
                                                                                                    insertUserData.get(String.valueOf(update.getMessage().getChatId())).add(update.getMessage().getText());
                                                                                                    break;

                                                                                                default:
                                                                                                    insertUserData.get(String.valueOf(update.getMessage().getChatId())).set(2, update.getMessage().getText());
                                                                                                    break;
                                                                                            }

                                                                                            inputPhone(String.valueOf(update.getMessage().getChatId()));
                                                                                            flow.put(String.valueOf(update.getMessage().getChatId()), "phone");
                                                                                        } else
                                                                                            //input phone
                                                                                            if (flow.get(String.valueOf(update.getMessage().getChatId())).equals("phone")) {
                                                                                                switch (insertUserData.get(String.valueOf(update.getMessage().getChatId())).size()) {
                                                                                                    case 3:
                                                                                                        insertUserData.get(String.valueOf(update.getMessage().getChatId())).add(update.getMessage().getText());
                                                                                                        break;

                                                                                                    default:
                                                                                                        insertUserData.get(String.valueOf(update.getMessage().getChatId())).set(3, update.getMessage().getText());
                                                                                                        break;
                                                                                                }

                                                                                                inputEmail(String.valueOf(update.getMessage().getChatId()));
                                                                                                flow.put(String.valueOf(update.getMessage().getChatId()), "email");
                                                                                            } else
                                                                                                //input email
                                                                                                if (flow.get(String.valueOf(update.getMessage().getChatId())).equals("email")) {
                                                                                                    switch (insertUserData.get(String.valueOf(update.getMessage().getChatId())).size()) {
                                                                                                        case 4:
                                                                                                            insertUserData.get(String.valueOf(update.getMessage().getChatId())).add(update.getMessage().getText());
                                                                                                            break;

                                                                                                        default:
                                                                                                            insertUserData.get(String.valueOf(update.getMessage().getChatId())).set(4, update.getMessage().getText());
                                                                                                            break;
                                                                                                    }

                                                                                                    registerUser(String.valueOf(update.getMessage().getChatId()));
                                                                                                } else
                                                                                                    //update user details
                                                                                                    //update email
                                                                                                    if (flow.get(String.valueOf(update.getMessage().getChatId())).equals("updateEmail")) {
                                                                                                        switch (updateUserData.get(String.valueOf(update.getMessage().getChatId())).size()) {
                                                                                                            case 0:
                                                                                                                updateUserData.get(String.valueOf(update.getMessage().getChatId())).add(update.getMessage().getText());
                                                                                                                break;

                                                                                                            default:
                                                                                                                updateUserData.get(String.valueOf(update.getMessage().getChatId())).set(0, update.getMessage().getText());
                                                                                                                break;
                                                                                                        }
                                                                                                        inputUpdatePhone(String.valueOf(update.getMessage().getChatId()));
                                                                                                        flow.put(String.valueOf(update.getMessage().getChatId()), "updatePhone");
                                                                                                    } else
                                                                                                        //update phone
                                                                                                        if (flow.get(String.valueOf(update.getMessage().getChatId())).equals("updatePhone")) {//change email n phone
                                                                                                            switch (updateUserData.get(String.valueOf(update.getMessage().getChatId())).size()) {
                                                                                                                case 1:
                                                                                                                    updateUserData.get(String.valueOf(update.getMessage().getChatId())).add(update.getMessage().getText());
                                                                                                                    break;

                                                                                                                default:
                                                                                                                    updateUserData.get(String.valueOf(update.getMessage().getChatId())).set(1, update.getMessage().getText());
                                                                                                                    break;
                                                                                                            }

                                                                                                            updateUser(String.valueOf(update.getMessage().getChatId()));
                                                                                                        } else

                                                                                                            //start the flow - booking (user)
                                                                                                            //input room
                                                                                                            if (flow.get(String.valueOf(update.getMessage().getChatId())).equals("room")) {
                                                                                                                switch (insertBookingData.get(String.valueOf(update.getMessage().getChatId())).size()) {
                                                                                                                    case 0:
                                                                                                                        insertBookingData.get(String.valueOf(update.getMessage().getChatId())).add(update.getMessage().getText());
                                                                                                                        break;

                                                                                                                    default:
                                                                                                                        insertBookingData.get(String.valueOf(update.getMessage().getChatId())).set(0, update.getMessage().getText());
                                                                                                                        break;
                                                                                                                }

                                                                                                                inputPurpose(String.valueOf(update.getMessage().getChatId()));
                                                                                                                flow.put(String.valueOf(update.getMessage().getChatId()), "purpose");
                                                                                                            } else
                                                                                                                //input purpose
                                                                                                                if (flow.get(String.valueOf(update.getMessage().getChatId())).equals("purpose")) {
                                                                                                                    switch (insertBookingData.get(String.valueOf(update.getMessage().getChatId())).size()) {
                                                                                                                        case 1:
                                                                                                                            insertBookingData.get(String.valueOf(update.getMessage().getChatId())).add(update.getMessage().getText());
                                                                                                                            break;

                                                                                                                        default:
                                                                                                                            insertBookingData.get(String.valueOf(update.getMessage().getChatId())).set(1, update.getMessage().getText());
                                                                                                                            break;
                                                                                                                    }

                                                                                                                    inputDate(String.valueOf(update.getMessage().getChatId()));
                                                                                                                    flow.put(String.valueOf(update.getMessage().getChatId()), "date");
                                                                                                                } else
                                                                                                                    //input date
                                                                                                                    if (flow.get(String.valueOf(update.getMessage().getChatId())).equals("date")) {
                                                                                                                        switch (insertBookingData.get(String.valueOf(update.getMessage().getChatId())).size()) {
                                                                                                                            case 2:
                                                                                                                                insertBookingData.get(String.valueOf(update.getMessage().getChatId())).add(update.getMessage().getText());
                                                                                                                                break;

                                                                                                                            default:
                                                                                                                                insertBookingData.get(String.valueOf(update.getMessage().getChatId())).set(2, update.getMessage().getText());
                                                                                                                                break;
                                                                                                                        }

                                                                                                                        inputTime(String.valueOf(update.getMessage().getChatId()));
                                                                                                                        flow.put(String.valueOf(update.getMessage().getChatId()), "time");
                                                                                                                    } else
                                                                                                                        //input time
                                                                                                                        if (flow.get(String.valueOf(update.getMessage().getChatId())).equals("time")) {
                                                                                                                            switch (insertBookingData.get(String.valueOf(update.getMessage().getChatId())).size()) {
                                                                                                                                case 3:
                                                                                                                                    insertBookingData.get(String.valueOf(update.getMessage().getChatId())).add(update.getMessage().getText());
                                                                                                                                    break;

                                                                                                                                default:
                                                                                                                                    insertBookingData.get(String.valueOf(update.getMessage().getChatId())).set(3, update.getMessage().getText());
                                                                                                                                    break;
                                                                                                                            }

                                                                                                                            bookingSuccess(String.valueOf(update.getMessage().getChatId()));
                                                                                                                        } else

                                                                                                                            //start the flow - cancel booking
                                                                                                                            if (flow.get(String.valueOf(update.getMessage().getChatId())).equals("cancel")) {

                                                                                                                                switch (deleteUserData.get(String.valueOf(update.getMessage().getChatId())).size()) {
                                                                                                                                    case 0:
                                                                                                                                        deleteUserData.get(String.valueOf(update.getMessage().getChatId())).add(update.getMessage().getText());
                                                                                                                                        break;

                                                                                                                                    default:
                                                                                                                                        deleteUserData.get(String.valueOf(update.getMessage().getChatId())).set(0, update.getMessage().getText());
                                                                                                                                        break;
                                                                                                                                }

                                                                                                                                bookingCancel(String.valueOf(update.getMessage().getChatId()));
                                                                                                                            } else

                                                                                                                                //start the flow - admin
                                                                                                                                //after entering command 3, then jump to admin
                                                                                                                                if (flow.get(String.valueOf(update.getMessage().getChatId())).equals("admin")) {
                                                                                                                                    switch (insertAdminData.get(String.valueOf(update.getMessage().getChatId())).size()) {
                                                                                                                                        case 0:
                                                                                                                                            insertAdminData.get(String.valueOf(update.getMessage().getChatId())).add(update.getMessage().getText());
                                                                                                                                            break;

                                                                                                                                        default:
                                                                                                                                            insertAdminData.get(String.valueOf(update.getMessage().getChatId())).set(0, update.getMessage().getText());
                                                                                                                                            break;
                                                                                                                                    }

                                                                                                                                    inputSchool(String.valueOf(update.getMessage().getChatId()));
                                                                                                                                    flow.put(String.valueOf(update.getMessage().getChatId()), "school");

                                                                                                                                    //validate whether the person who wants to register as admin is one of the users or not
                                                                                                                                    connect.checkUser(insertAdminData.get(update.getMessage().getChatId().toString()).get(0));
                                                                                                                                    String checkUserBeforeRegisterAsAdmin = connect.checkUser(insertAdminData.get(update.getMessage().getChatId().toString()).get(0));
                                                                                                                                    if (checkUserBeforeRegisterAsAdmin.equals("true")) {
                                                                                                                                        inputSchool(String.valueOf(update.getMessage().getChatId()));
                                                                                                                                        flow.put(String.valueOf(update.getMessage().getChatId()), "school");
                                                                                                                                    } else if (checkUserBeforeRegisterAsAdmin.equals("false")) {
                                                                                                                                        notAUser(String.valueOf(update.getMessage().getChatId()));
                                                                                                                                    }
                                                                                                                                } else
                                                                                                                                    //input school
                                                                                                                                    if (flow.get(String.valueOf(update.getMessage().getChatId())).equals("school")) {
                                                                                                                                        switch (insertAdminData.get(String.valueOf(update.getMessage().getChatId())).size()) {
                                                                                                                                            case 1:
                                                                                                                                                insertAdminData.get(String.valueOf(update.getMessage().getChatId())).add(update.getMessage().getText());
                                                                                                                                                break;

                                                                                                                                            default:
                                                                                                                                                insertAdminData.get(String.valueOf(update.getMessage().getChatId())).set(1, update.getMessage().getText());
                                                                                                                                                break;
                                                                                                                                        }

                                                                                                                                        //validate whether the school has admin already or not
                                                                                                                                        //one school has only one admin
                                                                                                                                        connect.checkAdmin(insertAdminData.get(update.getMessage().getChatId().toString()).get(1));
                                                                                                                                        String checkAdminStatus = connect.checkAdmin(insertAdminData.get(update.getMessage().getChatId().toString()).get(1));
                                                                                                                                        if (checkAdminStatus.equals("false")) {
                                                                                                                                            registerAdmin(String.valueOf(update.getMessage().getChatId()));
                                                                                                                                        } else if (checkAdminStatus.equals("true")) {
                                                                                                                                            adminAlreadyExist(String.valueOf(update.getMessage().getChatId()));
                                                                                                                                        }

                                                                                                                                    } else
                                                                                                                                        //add available room
                                                                                                                                        if (flow.get(String.valueOf(update.getMessage().getChatId())).equals("addroom")) {
                                                                                                                                            switch (insertRoomData.get(String.valueOf(update.getMessage().getChatId())).size()) {
                                                                                                                                                case 0:
                                                                                                                                                    insertRoomData.get(String.valueOf(update.getMessage().getChatId())).add(update.getMessage().getText());
                                                                                                                                                    break;

                                                                                                                                                default:
                                                                                                                                                    insertRoomData.get(String.valueOf(update.getMessage().getChatId())).set(0, update.getMessage().getText());
                                                                                                                                                    break;
                                                                                                                                            }

                                                                                                                                            inputRoomID(String.valueOf(update.getMessage().getChatId()));
                                                                                                                                            flow.put(String.valueOf(update.getMessage().getChatId()), "roomid");
                                                                                                                                        } else
                                                                                                                                            //input room id
                                                                                                                                            if (flow.get(String.valueOf(update.getMessage().getChatId())).equals("roomid")) {
                                                                                                                                                switch (insertRoomData.get(String.valueOf(update.getMessage().getChatId())).size()) {
                                                                                                                                                    case 1:
                                                                                                                                                        insertRoomData.get(String.valueOf(update.getMessage().getChatId())).add(update.getMessage().getText());
                                                                                                                                                        break;

                                                                                                                                                    default:
                                                                                                                                                        insertRoomData.get(String.valueOf(update.getMessage().getChatId())).set(1, update.getMessage().getText());
                                                                                                                                                        break;
                                                                                                                                                }

                                                                                                                                                inputRoomDescription(String.valueOf(update.getMessage().getChatId()));
                                                                                                                                                flow.put(String.valueOf(update.getMessage().getChatId()), "roomdescription");
                                                                                                                                            } else
                                                                                                                                                //input room description
                                                                                                                                                if (flow.get(String.valueOf(update.getMessage().getChatId())).equals("roomdescription")) {
                                                                                                                                                    switch (insertRoomData.get(String.valueOf(update.getMessage().getChatId())).size()) {
                                                                                                                                                        case 2:
                                                                                                                                                            insertRoomData.get(String.valueOf(update.getMessage().getChatId())).add(update.getMessage().getText());
                                                                                                                                                            break;

                                                                                                                                                        default:
                                                                                                                                                            insertRoomData.get(String.valueOf(update.getMessage().getChatId())).set(2, update.getMessage().getText());
                                                                                                                                                            break;
                                                                                                                                                    }

                                                                                                                                                    inputMaximumCapacity(String.valueOf(update.getMessage().getChatId()));
                                                                                                                                                    flow.put(String.valueOf(update.getMessage().getChatId()), "maximumcapacity");
                                                                                                                                                } else
                                                                                                                                                    //input maximum capacity
                                                                                                                                                    if (flow.get(String.valueOf(update.getMessage().getChatId())).equals("maximumcapacity")) {
                                                                                                                                                        switch (insertRoomData.get(String.valueOf(update.getMessage().getChatId())).size()) {
                                                                                                                                                            case 3:
                                                                                                                                                                insertRoomData.get(String.valueOf(update.getMessage().getChatId())).add(update.getMessage().getText());
                                                                                                                                                                break;

                                                                                                                                                            default:
                                                                                                                                                                insertRoomData.get(String.valueOf(update.getMessage().getChatId())).set(3, update.getMessage().getText());
                                                                                                                                                                break;
                                                                                                                                                        }

                                                                                                                                                        addRoomSuccess(String.valueOf(update.getMessage().getChatId()));
                                                                                                                                                    } else

                                                                                                                                                        //start the flow - update room (admin)
                                                                                                                                                        //check room id
                                                                                                                                                        if (flow.get(String.valueOf(update.getMessage().getChatId())).equals("updateCheckRoomID")) {
                                                                                                                                                            switch (updateRoomData.get(String.valueOf(update.getMessage().getChatId())).size()) {
                                                                                                                                                                case 0:
                                                                                                                                                                    updateRoomData.get(String.valueOf(update.getMessage().getChatId())).add(update.getMessage().getText());
                                                                                                                                                                    break;

                                                                                                                                                                default:
                                                                                                                                                                    updateRoomData.get(String.valueOf(update.getMessage().getChatId())).set(0, update.getMessage().getText());
                                                                                                                                                                    break;
                                                                                                                                                            }

                                                                                                                                                            //validate whether there is room to be updated
                                                                                                                                                            connect.checkRoomID(updateRoomData.get(update.getMessage().getChatId().toString()).get(0));
                                                                                                                                                            String checkroomstatus = connect.checkRoomID(updateRoomData.get(update.getMessage().getChatId().toString()).get(0));
                                                                                                                                                            if (checkroomstatus.equals("true")) {
                                                                                                                                                                updateRoomDesc(String.valueOf(update.getMessage().getChatId()));
                                                                                                                                                                flow.put(update.getMessage().getChatId().toString(), "updateroomdescription");
                                                                                                                                                            } else if (checkroomstatus.equals("false")) {
                                                                                                                                                                roomNotExist(String.valueOf(update.getMessage().getChatId()));
                                                                                                                                                            }

                                                                                                                                                        } else
                                                                                                                                                            //update room description
                                                                                                                                                            if (flow.get(String.valueOf(update.getMessage().getChatId())).equals("updateroomdescription")) {
                                                                                                                                                                switch (updateRoomData.get(String.valueOf(update.getMessage().getChatId())).size()) {
                                                                                                                                                                    case 1:
                                                                                                                                                                        updateRoomData.get(String.valueOf(update.getMessage().getChatId())).add(update.getMessage().getText());
                                                                                                                                                                        break;

                                                                                                                                                                    default:
                                                                                                                                                                        updateRoomData.get(String.valueOf(update.getMessage().getChatId())).set(1, update.getMessage().getText());
                                                                                                                                                                        break;
                                                                                                                                                                }

                                                                                                                                                                updateMaxCap(String.valueOf(update.getMessage().getChatId()));
                                                                                                                                                                flow.put(String.valueOf(update.getMessage().getChatId()), "updatemaximumcapacity");
                                                                                                                                                            } else
                                                                                                                                                                //update maximum capacity
                                                                                                                                                                if (flow.get(String.valueOf(update.getMessage().getChatId())).equals("updatemaximumcapacity")) {
                                                                                                                                                                    switch (updateRoomData.get(String.valueOf(update.getMessage().getChatId())).size()) {
                                                                                                                                                                        case 2:
                                                                                                                                                                            updateRoomData.get(String.valueOf(update.getMessage().getChatId())).add(update.getMessage().getText());
                                                                                                                                                                            break;

                                                                                                                                                                        default:
                                                                                                                                                                            updateRoomData.get(String.valueOf(update.getMessage().getChatId())).set(2, update.getMessage().getText());
                                                                                                                                                                            break;
                                                                                                                                                                    }

                                                                                                                                                                    updateRoomSuccess(String.valueOf(update.getMessage().getChatId()));
                                                                                                                                                                }
        }

        try {
            execute(sendMessage);
        } catch (TelegramApiException E) {
            E.printStackTrace();
        }
    }

    /**
     * This method is used to set text for Telegram Bot for command '/start'.
     */
    public void commandStart(String chatID) {
        sendMessage.setText("Hi! I'm X bot.\nWelcome to UUM Meeting Room Booking System." +
                "\nWe operate as usual from Sunday to Thursday, 8am - 5pm." +
                "\n\nYou may choose the options from the Menu:" +
                "\n\nFor USER," +
                "\n------------------------------------" +
                "\nClick '/registeruser' for the user registration section." +
                "\nClick '/updateuser' for the update user registration section." +
                "\nClick '/booking' for the booking section." +
                "\nClick '/bookinglist' for display booking list." +
                "\nClick '/cancel' for the cancel booking (only if you have done booking)." +
                "\nClick '/roomlist' for display available room list." +
                "\n\nFor ADMIN," +
                "\n------------------------------------" +
                "\nClick '/registeradmin' for the school admin registration section." +
                "\nClick '/addroom' for the add meeting room section (by school admin only)." +
                "\nClick '/updateroom' for the update meeting room section (by school admin only)." +
                "\nClick '/bookinglist' for checking the booking list.");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot for command '/registeruser'.
     */
    public void commandRegisterUser(String chatID) {
        sendMessage.setText("Let's proceed with user registration section!" +
                "\n\nImportant note:" +
                "\nPlease ensure that all information you have entered is correct, no modification provided after registration." +
                "\n\nAre you sure to register?" +
                "\n\nReply 1: Yes" +
                "\nReply 2: No");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot for command '/updateuser'.
     */
    public void commandUpdateUser(String chatID) {
        sendMessage.setText("Let's proceed with user details update section!" +
                "\n\nImportant note:" +
                "\nYou are only allowed to update your phone number and email." +
                "\n\nAre you sure to proceed update?" +
                "\n\nReply 7: Yes" +
                "\nReply 8: No");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot for command '/registeradmin'.
     */
    public void commandRegisterAdmin(String chatID) {
        sendMessage.setText("Let's proceed with admin registration section!" +
                "\n\nImportant note:" +
                "\nPlease ensure that all information you have entered is correct, no modification provided after registration." +
                "\n(Only one admin can be applied per school)" +
                "\n\nAre you sure to register?" +
                "\n\nReply 3: Yes" +
                "\nReply 4: No");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot for command '/booking'.
     */
    public void commandBooking(String chatID) {
        sendMessage.setText("Let's proceed with booking section!" +
                "\n\nImportant note:" +
                "\nPlease ensure that all information you have entered is correct, no modification provided during the booking process." +
                "\n\nAre you sure want to book the meeting room right now?" +
                "\n\nReply Y: Yes" +
                "\nReply N: No");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot for command '/addroom'.
     */
    public void commandAddRoom(String chatID) {
        sendMessage.setText("Let's proceed with add meeting room section!" +
                "\n\nImportant note:" +
                "\nPlease ensure that all information you have entered is correct, no modification provided after submit." +
                "\n(Only school admin can add meeting room for their own school)" +
                "\n\nAre you sure to add meeting room right now?" +
                "\n\nReply 5: Yes" +
                "\nReply 6: No");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot for command '/updateroom'.
     */
    public void commandUpdateRoom(String chatID) {
        sendMessage.setText("You may update booking meeting room if you wish to do." +
                "\n\nImportant note:" +
                "\nPlease ensure that all information you have entered is correct, no modification provided after submit." +
                "\n(Only school admin can update meeting room for their own school)" +
                "\n\nAre you sure to update meeting room right now?" +
                "\n\nReply 9: Yes" +
                "\nReply 10: No");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot for command '/cancel'.
     */
    public void commandCancel(String chatID) {
        sendMessage.setText("Please enter your staff id to cancel your booking.");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot for command '/bookinglist'.
     */
    public void commandDisplay(String chatID) {
        sendMessage.setText("The users booking list has been shown as below:\n\n" + connect.displayList() +
                "\nYou may click '/start' to go back to Main Menu." +
                "\nIf you like to cancel your booking, just click '/cancel' in the Menu.");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot for command '/roomlist'.
     */
    public void commandDisplayAvailbleRoom(String chatID) {
        sendMessage.setText("The available room list has been shown as below:\n\n" + connect.displayAvailableRoomList() +
                "\nYou may click '/start' to go back to Main Menu." +
                "\nIf you like to update any available room, just click '/updateroom' in the Menu.");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot for quit.
     */
    public void commandQuit(String chatID) {
        sendMessage.setText("The section has ended." +
                "\nPlease click '/start' in the Menu again to back to Main Menu.");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to prompt the user enter their name.
     */
    public void inputName(String chatID) {
        sendMessage.setText("(A) First of all, may I have your name please?" +
                "\ne.g.: Tan Xue Lee" +
                "\n\nReply 0: If you do not wish to proceed with the registration");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to prompt the user enter their IC number.
     */
    public void inputIC(String chatID) {
        sendMessage.setText("(B) How about your IC number?" +
                "\ne.g.: 901225025256" +
                "\n\nReply 0: If you do not wish to proceed with the registration");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to prompt the user enter their staff ID.
     */
    public void inputStaffID(String chatID) {
        sendMessage.setText("(C) How about your staff ID number?" +
                "\ne.g.: 278504" +
                "\n\nReply 0: If you do not wish to proceed with the registration");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to prompt the user enter their phone number.
     */
    public void inputPhone(String chatID) {
        sendMessage.setText("(D) Kindly provide your phone number." +
                "\ne.g.: 0194567891" +
                "\n\nReply 0: If you do not wish to proceed with the registration");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to prompt the user update their phone number.
     */
    public void inputUpdatePhone(String chatID) {
        sendMessage.setText("(B) Kindly update your phone number." +
                "\ne.g.: 0194567891" +
                "\n\nReply 0: If you do not wish to proceed with the update");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to prompt the user enter their email address.
     */
    public void inputEmail(String chatID) {
        sendMessage.setText("(E) Kindly provide your email address." +
                "\ne.g.: xuelee@gmail.com" +
                "\n\nReply 0: If you do not wish to proceed with the registration");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to prompt the user update their email address.
     */
    public void inputUpdateEmail(String chatID) {
        sendMessage.setText("(A) Kindly update your email address." +
                "\ne.g.: xuelee@gmail.com" +
                "\n\nReply 0: If you do not wish to proceed with the update");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to prompt the user input room id.
     */
    public void inputRoom(String chatID) {
        sendMessage.setText("(A) Please choose which meeting rooms that you want to book.\n" + connect.displayRoom() +
                "\nReply 0: If you do not wish to proceed with the booking");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to prompt the user enter their booking purpose.
     */
    public void inputPurpose(String chatID) {
        sendMessage.setText("(B) Please provide the purpose of booking for the meeting room." +
                "\ne.g.: conference / training session / brainstorming" +
                "\n\nReply 0: If you do not wish to proceed with the booking");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to prompt the user enter booking date.
     */
    public void inputDate(String chatID) {
        sendMessage.setText("(C) Please provide a preferred date for the booking." +
                "\nPlease enter with the following format (dd-mm-yyyy):" +
                "\ne.g.: 31-12-2022" +
                "\n\nReply 0: If you do not wish to proceed with the booking");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to prompt the user enter booking time.
     */
    public void inputTime(String chatID) {
        sendMessage.setText("(D) Please provide a preferred time for the booking." +
                "\nPlease enter with the following format (hh:mma):" +
                "\ne.g.: 09:00AM" +
                "\n\nReply 0: If you do not wish to proceed with the booking");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to show the user registration details.
     */
    public void registerUser(String chatID) {
        connect.insertUser(insertUserData.get(chatID).get(0), insertUserData.get(chatID).get(1), insertUserData.get(chatID).get(2), insertUserData.get(chatID).get(3), insertUserData.get(chatID).get(4));
        sendMessage.setText("Your user registration has been recorded, the details will be shown as below, thank you!" +
                "\n\nName: " + insertUserData.get(chatID).get(0) +
                "\nIC Number: " + insertUserData.get(chatID).get(1) +
                "\nStaff ID: " + insertUserData.get(chatID).get(2) +
                "\nPhone Number: " + insertUserData.get(chatID).get(3) +
                "\nEmail: " + insertUserData.get(chatID).get(4) +
                "\n\nYou may choose the functions that you want in the Menu." +
                "\nIf not, you may click '/start' in the Menu again to back to Main Menu.");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to show the updated user registration details.
     */
    public void updateUser(String chatID) {
        connect.updateUser(updateUserData.get(chatID).get(0), updateUserData.get(chatID).get(1), insertUserData.get(chatID).get(2));
        sendMessage.setText("Your updated user details has been recorded, your personal details will be shown as below, thank you!" +
                "\n\nNew Email: " + updateUserData.get(chatID).get(0) +
                "\n\nNew Phone: " + updateUserData.get(chatID).get(1) +
                "\n\nYou may book a meeting room through '/booking' in the Menu." +
                "\nIf you like to cancel your booking, just click '/cancel' in the Menu.");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot after user has successfully booking, and the data will be saved in database.
     */
    public void bookingSuccess(String chatID) {
        connect.insertBooking(insertBookingData.get(chatID).get(0), insertBookingData.get(chatID).get(1), insertBookingData.get(chatID).get(2), insertBookingData.get(chatID).get(3), insertUserData.get(chatID).get(2));
        sendMessage.setText("Your booking has been recorded, the details will be shown as below, thank you!" +
                "\nRoom Type: " + insertBookingData.get(chatID).get(0) +
                "\nBooking Purpose: " + insertBookingData.get(chatID).get(1) +
                "\nBooking Date: " + insertBookingData.get(chatID).get(2) +
                "\nBooking Time: " + insertBookingData.get(chatID).get(3) +
                "\n\nYou may choose the functions that you want in the Menu." +
                "\nIf not, you may click '/start' in the Menu again to back to Main Menu.");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot after the user enter their staff id for cancel booking, and the data will be deleted from database.
     */
    public void bookingCancel(String chatID) {
        connect.deleteBooking(deleteUserData.get(chatID).get(0));
        sendMessage.setText("Your booking has been cancelled." +
                "\n\nPlease click '/booking' in the Menu if you want to book a meeting room again.");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to prompt the user enter their staff ID for admin registration.
     */
    public void inputAdmin(String chatID) {
        sendMessage.setText("May I know your staff ID number?" +
                "\ne.g.: 278504" +
                "\n\nReply 0: If you do not wish to proceed with the registration");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to prompt the user enter their school ID for admin registration.
     */
    public void inputSchool(String chatID) {
        sendMessage.setText("Which school do you want to apply for?" +
                "\nReply 'COB1' for School of Business Management (SBM)" +
                "\nReply 'COB2' for Tunku Puteri Intan Safinaz School of Accountancy (TISSA)" +
                "\nReply 'COB3' for School of Economics, Finance and Banking (SEFB)" +
                "\nReply 'COB4' for Islamic Business School (IBS)" +
                "\nReply 'COB5' for School of Technology Management and Logistics (STML)" +
                "\nReply 'COB6' for National Golf Academy (NGA)" +
                "\nReply 'CAS1' for School of Creative Industry Management and Performing Arts (SCIMPA)" +
                "\nReply 'CAS2' for School of Multimedia Technology & Communication (SMMTC)" +
                "\nReply 'CAS3' for School of Applied Psychology, Social Work & Policy (SAPSP)" +
                "\nReply 'CAS4' for School of Quantitative Sciences (SQS)" +
                "\nReply 'CAS5' for School of Education (SOE)" +
                "\nReply 'CAS6' for School of Computing (SOC)" +
                "\nReply 'CAS7' for School of Languages, Civilisation and Philosophy (SLCP)" +
                "\nReply 'COLGIS1' for School of Law (SOL)" +
                "\nReply 'COLGIS2' for School of International Studies (SOIS)" +
                "\nReply 'COLGIS3' for School of Government (SOG)" +
                "\nReply 'COLGIS3' for School of Tourism, Hospitality and Event Management (STHEM)" +
                "\n\nReply 0: If you do not wish to proceed with the registration");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to show the admin registration details.
     */
    public void registerAdmin(String chatID) {
        connect.insertAdmin(insertAdminData.get(chatID).get(0), insertAdminData.get(chatID).get(1));
        sendMessage.setText("Your admin registration has been recorded, the details will be shown as below, thank you!" +
                "\n\nStaff ID: " + insertAdminData.get(chatID).get(0) +
                "\nSchool ID: " + insertAdminData.get(chatID).get(1) +
                "\n\nYou may choose the functions that you want in the Menu." +
                "\nIf not, you may click '/start' in the Menu again to back to Main Menu.");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to show adding the room by knowing which school that the admin from.
     */
    public void addRoom(String chatID) {
        sendMessage.setText("May I know which school admin you are?" +
                "\nReply 'COB1' for School of Business Management (SBM)" +
                "\nReply 'COB2' for Tunku Puteri Intan Safinaz School of Accountancy (TISSA)" +
                "\nReply 'COB3' for School of Economics, Finance and Banking (SEFB)" +
                "\nReply 'COB4' for Islamic Business School (IBS)" +
                "\nReply 'COB5' for School of Technology Management and Logistics (STML)" +
                "\nReply 'COB6' for National Golf Academy (NGA)" +
                "\nReply 'CAS1' for School of Creative Industry Management and Performing Arts (SCIMPA)" +
                "\nReply 'CAS2' for School of Multimedia Technology & Communication (SMMTC)" +
                "\nReply 'CAS3' for School of Applied Psychology, Social Work & Policy (SAPSP)" +
                "\nReply 'CAS4' for School of Quantitative Sciences (SQS)" +
                "\nReply 'CAS5' for School of Education (SOE)" +
                "\nReply 'CAS6' for School of Computing (SOC)" +
                "\nReply 'CAS7' for School of Languages, Civilisation and Philosophy (SLCP)" +
                "\nReply 'COLGIS1' for School of Law (SOL)" +
                "\nReply 'COLGIS2' for School of International Studies (SOIS)" +
                "\nReply 'COLGIS3' for School of Government (SOG)" +
                "\nReply 'COLGIS3' for School of Tourism, Hospitality and Event Management (STHEM)" +
                "\n\nReply 0: If you do not wish to proceed with the add meeting room section");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to prompt the admin enter room ID to add the room.
     */
    public void inputRoomID(String chatID) {
        sendMessage.setText("Please provide the meeting room ID number." +
                "\nPlease enter with the following format (school in short form + number):" +
                "\ne.g.: SOC1" +
                "\n\nReply 0: If you do not wish to proceed with the add meeting room section");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to prompt the admin enter room description to add the room.
     */
    public void inputRoomDescription(String chatID) {
        sendMessage.setText("Please provide the meeting room description." +
                "\nPlease enter with the following format (room type + number):" +
                "\ne.g.: Study Hall 1 / Lounge 1 / Lecturer Hall 1" +
                "\n\nReply 0: If you do not wish to proceed with the add meeting room section");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to prompt the admin enter maximum capacity to add the room.
     */
    public void inputMaximumCapacity(String chatID) {
        sendMessage.setText("Please provide the maximum capacity of the meeting room." +
                "\ne.g.: 50" +
                "\n\nReply 0: If you do not wish to proceed with the add meeting room section");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot after admin has successfully added the room, and the data will be saved in database.
     */
    public void addRoomSuccess(String chatID) {
        connect.insertRoom(insertRoomData.get(chatID).get(1), insertRoomData.get(chatID).get(2), insertRoomData.get(chatID).get(3), insertRoomData.get(chatID).get(0));
        sendMessage.setText("New meeting room has been added, the details will be shown as below, thank you!" +
                "\n\nSchool ID: " + insertRoomData.get(chatID).get(0) +
                "\nRoom ID: " + insertRoomData.get(chatID).get(1) +
                "\nRoom description: " + insertRoomData.get(chatID).get(2) +
                "\nMaximum capacity: " + insertRoomData.get(chatID).get(3) +
                "\n\nYou may choose the functions that you want in the Menu." +
                "\nIf not, you may click '/start' in the Menu again to back to Main Menu.");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to prompt the admin update room by checking room id.
     */
    public void updateCheckRoomID(String chatID) {
        sendMessage.setText("You may check the meeting room ID number." +
                "\nPlease enter with the following format (school in short form + number):" +
                "\ne.g.: SOC1" +
                "\n\nReply 0: If you do not wish to proceed with the update meeting room section");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to prompt the admin update room description.
     */
    public void updateRoomDesc(String chatID) {
        sendMessage.setText("You may check a new room description." +
                "\nPlease enter with the following format (room type + number):" +
                "\ne.g.: Study Hall 1 / Lounge 1 / Lecturer Hall 1" +
                "\n\nReply 0: If you do not wish to proceed with the update meeting room section");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to prompt the room is not existed.
     */
    public void roomNotExist(String chatID) {
        sendMessage.setText("Room does not exist." +
                "\n\nYou may choose the functions that you want in the Menu." +
                "\nIf not, you may click '/start' in the Menu again to back to Main Menu.");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to prompt the admin update maximum capacity.
     */
    public void updateMaxCap(String chatID) {
        sendMessage.setText("Please enter maximum capacity of the meeting room for update." +
                "\ne.g.: 50" +
                "\n\nReply 0: If you do not wish to proceed with the add meeting room section");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot after admin has successfully updated the room, and the data will be saved in database.
     */
    public void updateRoomSuccess(String chatID) {
        connect.updateroom(updateRoomData.get(chatID).get(0), updateRoomData.get(chatID).get(1), updateRoomData.get(chatID).get(2));
        sendMessage.setText("Meeting room has been updated, the details will be shown as below, thank you!" +
                "\n\nRoom ID: " + updateRoomData.get(chatID).get(0) +
                "\nRoom description: " + updateRoomData.get(chatID).get(1) +
                "\nMaximum capacity: " + updateRoomData.get(chatID).get(2) +
                "\n\nYou may choose the functions that you want in the Menu." +
                "\nIf not, you may click '/start' in the Menu again to back to Main Menu.");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to prompt the admin for the school has been already existed as one school has only one admin.
     */
    public void adminAlreadyExist(String chatID) {
        sendMessage.setText("Sorry! One school can only has one admin." +
                "\n\nYou may choose the functions that you want in the Menu." +
                "\nIf not, you may click '/start' in the Menu again to back to Main Menu.");
        sendMessage.setChatId(chatID);
    }

    /**
     * This method is used to set text for Telegram Bot to prompt he or she is not a user, hence cannot be an admin.
     * the person has to register as user first.
     */
    public void notAUser(String chatID) {
        sendMessage.setText("Sorry! You are not a user." +
                "\n\nPlease register as a user before you register as your school admin by '/registeruser'." +
                "\nlnIf not, you may click '/start' in the Menu again to back to Main Menu.");
        sendMessage.setChatId(chatID);
    }
}