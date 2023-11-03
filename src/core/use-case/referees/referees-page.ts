import { Inject, Injectable } from "@nestjs/common";
import { Referees } from "src/core/entity/abstract-referees";
import { IRefereesRepository } from "src/core/repository/referees.repository";

@Injectable()
export class RefereesPage {
  constructor(@Inject() private refereesRepository: IRefereesRepository) {}

  getById = async (id: number): Promise<Referees> => {
    return this.refereesRepository.getById(id);
  };
}