import { useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";
import {
    Card,
    Typography,
    Button,
    TableContainer,
    TableHead,
    TableBody,
    TableRow,
    TableCell,
    CardActions,
} from '@mui/material';

import TablePagination, {
    tablePaginationClasses
} from "@mui/material/TablePagination";

import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import LogoutIcon from '@mui/icons-material/Logout';
import axios from "axios";
import AuthService from "../services/auth.service"
import EventBus from "../common/eventBus";
import useIdleTimeout from "../common/idleTimer"

const API_URL = process.env.REACT_APP_API_USERS_URL;
const IDLE_TIMEOUT = process.env.REACT_APP_USER_IDLE_TIMEOUT_S;

const Users = () => {
    const columns = [
        { id: 'icon', name: '' },
        { id: 'githubId', name: 'Id' },
        { id: 'login', name: 'Username' }
    ]

    interface IRow {
        id: string; 
        githubId: string; 
        login: string; 
    }

    const [rows, rowChange] = useState<IRow[]>([]);
    const [usersCount, setUsersCount] = useState(0);
    const [controller, setController] = useState({
        page: 0,
        rowsPerPage: 10
    });

    const [currentUser, setCurrentUser] = useState(undefined);

    function logOut() {
        AuthService.logout();
        setCurrentUser(undefined);
    }

    useEffect(() => {
        const user = AuthService.getCurrentUser();

        if (user) {
            setCurrentUser(user);
        }

        //attach logout event
        EventBus.on("logout", logOut);

        return () => {
            //detach logout event
            EventBus.remove("logout", logOut);
        };
    }, []);

    const handlePageChange = (event: React.MouseEvent<HTMLButtonElement> | null, newPage: number) => {
        if (!currentUser) {
            handleLogout();
        }
        setController({
            ...controller,
            page: newPage
        });
        idleTimer.reset();
    };

    const handleChangeRowsPerPage = (event: React.SyntheticEvent) => {
        if (!currentUser) {
            handleLogout();
        }
        let target = event.target as HTMLInputElement;
        setController({
            ...controller,
            rowsPerPage: parseInt(target.value, 10),
            page: 0
        });
        idleTimer.reset();
    };

    const navigate = useNavigate();

    const handleLogout = () => {
        setCurrentUser(undefined);
        AuthService.logout();
        navigate("/", { replace: true });
        window.location.reload();
    }

    useEffect(() => {
        const getData = async () => {
            const itemString = localStorage.getItem('user');
            const tokenValue = itemString ? JSON.parse(itemString).token : "";
            const apiUrl = API_URL ? API_URL : "";
            console.log(apiUrl);
            console.log(tokenValue);
            console.log(controller.page);
            await axios.get(apiUrl, {
                params: {
                    page: controller.page,
                    size: controller.rowsPerPage,
                    sort: "login"
                },
                headers: {
                    Authorization: "Bearer " + tokenValue
                }
            })
                .then(response => {
                    console.log(response);
                    console.log(response.data);
                    const rowData = response.data;
                    rowChange(rowData);
                    setUsersCount(1000);
                    return response;
                })
                .catch(error => {
                    console.log(error);
                    if (error.response && error.response.status === 401) {
                        //fire "logout" event since response got status UNAUTHORIZES
                        EventBus.dispatch("logout");
                    }
                });


        };
        getData();
    },
        [controller]
    );

    const handleOnIdle = () => {
        handleLogout();
    }

    const handleOnActive = () => {

    }


    //simply logout after idle timeout is expired
    let idleTimeValue: unknown = IDLE_TIMEOUT;
    const idleTimer = useIdleTimeout({ onIdle: handleOnIdle, onActive: handleOnActive, idleTime: idleTimeValue as number});


    return (
        <Card>
            <Typography variant="h3" component="div" align='center'>
                Users
            </Typography>
            <CardActions disableSpacing
                sx={{
                    alignSelf: "stretch",
                    display: "flex",
                    justifyContent: "flex-end",
                    alignItems: "flex-start",
                    p: 1,
                }}>
                <Button endIcon={<LogoutIcon />}
                    sx={{ marginTop: 3, borderRadius: 3 }}
                    variant="contained"
                    style={{ backgroundColor: 'black', color: 'white' }}
                    onClick={handleLogout}>
                    Logout
                </Button>
            </CardActions>

            <TableContainer>
                <TableHead>
                    <TableRow>
                        {columns.map(
                            (column) => (
                                <TableCell style={{ backgroundColor: 'black', color: 'white' }} key={column.id}>{column.name}</TableCell>
                            )
                        )}
                    </TableRow>
                </TableHead>
                <TableBody>
                    {rows.map((row) => {
                        return (
                            <TableRow key={row.id}>
                                <TableCell>
                                    <AccountCircleIcon />
                                </TableCell>
                                <TableCell>
                                    {row.githubId}
                                </TableCell>
                                <TableCell>
                                    {row.login}
                                </TableCell>
                            </TableRow>
                        )
                    })}
                </TableBody>
            </TableContainer>
            <TablePagination sx={{
                [`& .${tablePaginationClasses.spacer}`]: {
                    display: "none"
                },
                [`& .${tablePaginationClasses.toolbar}`]: {
                    justifyContent: "left"
                }
            }}
                component="div"
                onPageChange={handlePageChange}
                page={controller.page}
                count={usersCount}
                rowsPerPage={controller.rowsPerPage}
                onRowsPerPageChange={handleChangeRowsPerPage}
                rowsPerPageOptions={[10, 20, 30]}
            />
        </Card>
    )
}

export default Users;
