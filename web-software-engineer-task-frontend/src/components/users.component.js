import { Box, Paper, TableContainer, TableHead, TableRow, TableCell, TableBody, TablePagination } from "@mui/material"
import { useEffect, useState } from "react"

const Users = () => {
    const columns = [
        { id: 'githubId', name: 'Id' },
        { id: 'login', name: 'Username' }
    ]

    const [rows, rowChange] = useState([]);
    const [page, pageChange] = useState([0]);
    const [rowSize, rowSizeChange] = useState(5);

    const handleChangePage = (e, newPage) => {
        pageChange(newPage);
    }

    const handleRowsPerPageChange = (e) => {
        rowSizeChange(+e.target.value)
        pageChange(0);
    }

    useEffect(() => {
        fetch("http://localhost:8000/users").then(resp => {
            return resp.json();
        }
        ).then(
            resp => {
                rowChange(resp);
            }
        ).catch(
            e => {
                console.log(e);
            }
        )
    }
        , []
    )

    return (
        <div>
            <Box display={"flex"}
                flexDirection={"column"}
                alignItems={"center"}
                justifyContent={"center"}
                margin={"auth"}
                marginTop={5}
                padding={3}
                borderRadius={5}
                boxShadow={"5px 5px 10px #ccc"}
                sx={
                    {
                        ":hover": {
                            boxShadow: '10px 10px 20px #ccc'
                        }
                    }
                }>
                <Paper sx={{ with: '90%', marginLeft: '5%' }}>
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
                            {rows.length && rows
                                .slice(page * rowSize, page * rowSize + rowSize)
                                .map((row, i) => {
                                    return (
                                        <TableRow key={i}>
                                            {columns && columns.map((column, i) => {
                                                let value = row[column.id];
                                                return (
                                                    <TableCell key={value}>
                                                        {value}
                                                    </TableCell>
                                                )
                                            })}
                                        </TableRow>
                                    )
                                })}
                        </TableBody>
                    </TableContainer>

                    <TablePagination
                        rowsPerPageOptions={[5, 10, 25]} 
                        page={page} 
                        rowsPerPage={rowSize}
                        count={rows.length}
                        component="div" 
                        onPageChange={handleChangePage}
                        onRowsPerPageChange={handleRowsPerPageChange}
                    >

                    </TablePagination>
                </Paper>
            </Box>
        </div>
    )
}

export default Users