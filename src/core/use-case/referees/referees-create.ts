import { Inject, Injectable } from "@nestjs/common";
import { Referees } from "src/core/entity/abstract-referees";
import { IRefereesRepository } from "src/core/repository/referees.repository";

@Injectable()
export class RefereesCreate {
  constructor(@Inject() private refereesRepository: IRefereesRepository) {}

  create = async (input: Referees): Promise<Referees> => {
    return this.refereesRepository.create(input);
  };

  update = async (input: Referees): Promise<Referees> => {
    return this.refereesRepository.update(input);
  };
}
