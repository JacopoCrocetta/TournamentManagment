import { Inject, Injectable, Logger } from "@nestjs/common";
import { ITournamentsRepository } from "src/core/repository/tournaments.repository";

@Injectable()
export class DeleteTournament{
    constructor(@Inject() private repository: ITournamentsRepository) {}


    async deleteById(id: number):Promise<boolean>{
        Logger.log("DELETING ITEM WITH ID "+ id+" ...");
        this.repository.delete(id);

        Logger.log("CHECK IF THE ITEM WAS DELETED CORRECTLY...");
        const res = this.repository.getById(id) === null;

        return !!res;
    }
}