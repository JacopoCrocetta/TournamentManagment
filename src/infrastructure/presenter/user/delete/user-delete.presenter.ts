import { Logger } from "@nestjs/common";
import { IRefereesRepository } from "src/core/repository/referees.repository";
import { IUserRepository } from "src/core/repository/user.repository";

export class UserDeletePresenter {
    constructor(private repository: IUserRepository){}

    async deleteUserById(username: string, email:string): Promise<boolean>{
        Logger.log("TRYING TO DELETE THE USER WITH USERNAME " + username);
        return await this.repository.deleteUserByUserNameAndEmail(username, email);
    }
}