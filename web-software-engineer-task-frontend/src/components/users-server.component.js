import { useState, useEffect} from 'react';
import {
    Card,
    Typography,
    TableContainer,
    TableHead,
    TableBody,
    TableRow,
    TableCell,
} from '@mui/material';

import TablePagination, {
    tablePaginationClasses
} from "@mui/material/TablePagination";

import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import axios from "axios";

const API_URL = process.env.REACT_APP_API_USERS_URL;


const Users = () => {
    const columns = [
        { id: 'icon', name: ''},
        { id: 'githubId', name: 'Id' },
        { id: 'login', name: 'Username' }
    ]

    const [rows, rowChange] = useState([]);
    const [usersCount, setUsersCount] = useState(0);
    const [controller, setController] = useState({
        page: 0,
        rowsPerPage: 10
    });

    const handlePageChange = (event, newPage) => {
        setController({
            ...controller,
            page: newPage
        });
    };

    const handleChangeRowsPerPage = (event) => {
        setController({
            ...controller,
            rowsPerPage: parseInt(event.target.value, 10),
            page: 0
        });
    };

    useEffect(() => {
        const getData = async () => {
            console.log(JSON.parse(localStorage.getItem('user')).token);
            await axios.get(API_URL, {
                params: {
                    page: controller.page, 
                    size: controller.rowsPerPage, 
                    sort: "login"
                },
                headers: {
                    Authorization: "Bearer " + JSON.parse(localStorage.getItem('user')).token
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
                });


        };
        getData();
    },
    [controller]
    );


    return (
        <Card>
            <Typography variant="h3" component="div" align='center'>
                Users
            </Typography>
            <TableContainer alignItems={""} >
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

